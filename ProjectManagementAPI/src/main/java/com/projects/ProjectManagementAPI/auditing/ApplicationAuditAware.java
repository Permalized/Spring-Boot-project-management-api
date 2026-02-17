package com.projects.ProjectManagementAPI.auditing;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.projects.ProjectManagementAPI.user.User;


public class ApplicationAuditAware implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        // Here you can implement logic to retrieve the current user's ID from the security context
        // For example, if you're using Spring Security, you can get the user details and return the user ID
        // This is just a placeholder implementation and should be replaced with actual logic to retrieve the user ID
        //return Optional.of(1); // Replace with actual user ID retrieval logic
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken /*authentication.getPrincipal().equals("anonymousUser")*/) {
            return Optional.empty();
        }
        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }

}
