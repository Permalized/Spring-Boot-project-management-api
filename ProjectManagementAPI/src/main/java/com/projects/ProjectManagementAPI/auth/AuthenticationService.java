package com.projects.ProjectManagementAPI.auth;


import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.ProjectManagementAPI.config.JwtService;
import com.projects.ProjectManagementAPI.exceptions.AuthenticationFailedException;
import com.projects.ProjectManagementAPI.exceptions.DuplicateResourceException;
import com.projects.ProjectManagementAPI.token.Token;
import com.projects.ProjectManagementAPI.token.TokenRepository;
import com.projects.ProjectManagementAPI.token.TokenType;
import com.projects.ProjectManagementAPI.user.User;
import com.projects.ProjectManagementAPI.user.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    public AuthenticationResponse register(RegisterRequest request) {
        // Registration logic here
        var user=User.builder()
        .firstname(request.getFirstName())
        .lastname(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .build();
        
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new DuplicateResourceException("A user with this email already exists");
        }
        var savedUser=userRepository.save(user);
        var jwtToken=jwtService.generateToken(user);
        var refreshToken=jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .access_token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authentication logic here
        try{
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
             )
        );}
        catch(AuthenticationException e){
            throw new AuthenticationFailedException("The email or password you entered is incorrect");
        }
        var user=userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new AuthenticationFailedException("The email or password you entered is incorrect"));
        var jwtToken=jwtService.generateToken(user);
        var refreshToken=jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .access_token(jwtToken)
                .refreshToken(refreshToken)
                .build();
        
    }
    private void revokeAllUserTokens(User user) {
        var validUserTokens=tokenRepository.findAllValidTokenByUser(user.getId());
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token=Token.builder()
            .user(user)  
            .token(jwtToken)   
            .tokenType(TokenType.BEARER)
            .revoked(false)
            .expired(false)
            .build();
        tokenRepository.save(token);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException  {
        
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            return;
        }
        refreshToken=authHeader.substring(7);
        userEmail= jwtService.extractUsername(refreshToken);
        //SecurityContextHolder.getContext().getAuthentication() == null means that the uses is not authenticated and connected yer
        if (userEmail != null ) {
            var user = this.userRepository.findByEmail(userEmail).orElseThrow();
            // var isTokenValid = tokenRepository.findByToken(refreshToken)
            // .map(t -> !t.isExpired() && !t.isRevoked())
            // .orElse(false);
            if (jwtService.isTokenValid(refreshToken, user) /*  && isTokenValid */) {

                var accessToken=jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse=AuthenticationResponse.builder()
                    .access_token(accessToken)
                    .refreshToken(refreshToken)
                    .build();
                
                new ObjectMapper().writeValue(response.getOutputStream(),authResponse);

                // UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                // userDetails,
                // null,
                // userDetails.getAuthorities());
                // authToken.setDetails(
                // new WebAuthenticationDetailsSource().buildDetails(request)
                // );
                // SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
    }   

    

}
