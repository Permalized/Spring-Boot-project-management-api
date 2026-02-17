package com.projects.taskManager.user;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users") // Base URL for all user-related endpoints
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    //@PostMapping or PatchMapping for changing password(we are patching thes user)
    @PatchMapping
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) {

        service.changePassword(request, connectedUser);
        return ResponseEntity.accepted().build();


    }

}
