package com.projects.taskManager.demo;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/management") // Base URL for all management-related endpoints
@Tag(name="Management Controller", description = "Endpoints for management operations")
public class ManagementController {

    @Operation(
        description = "Create a new management resource",
        summary = "Create management resource",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully created management resource"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Unauthorized/ Invalid token"
            ),
        
        }
    )
    @PostMapping
    public String post(){
        return "Post:: management controller";
    }
     @GetMapping
    public String get(){
        return "Get:: management controller";
    }
    @PutMapping
    public String put() {
        return "Put:: management controller";
    }
    @DeleteMapping
    public String delete() {
        return "Delete:: management controller";
    }

}
