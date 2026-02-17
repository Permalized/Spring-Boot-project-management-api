package com.projects.taskManager.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/admin") // Base URL for all admin-related endpoints
@PreAuthorize("hasRole('ADMIN')") // Ensure that only users with the ADMIN role can access these endpoints
//@SecurityRequirement(name = "bearerAuth") // Specify that these endpoints require authentication with the bearerAuth security scheme, or centralize this at the OpenApiConfig level if all endpoints require authentication
//@Hidden // Hide this controller from OpenAPI documentation
public class AdminController {

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    
    public String post(){
        return "Post:: admin controller";
    }
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')") // Ensure that only users with the admin:read permission can access this endpoint
    public String get(){
        return "Get:: admin controller";
    }
    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    public String put() {
        return "Put:: admin controller";
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    public String delete() {
        return "Delete:: admin controller";
    }
    

}
