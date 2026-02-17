package com.projects.taskManager.user;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        // TODO Auto-generated method stub
        var user=(User)((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        //check if the current password is correct

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Current password is incorrect");
        }
        if(!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("New password and confirmation password do not match");
        }
        //update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        //save the new password
        repository.save(user);

        
    }



}
