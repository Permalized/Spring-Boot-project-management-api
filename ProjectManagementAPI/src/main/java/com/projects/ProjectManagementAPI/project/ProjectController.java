package com.projects.ProjectManagementAPI.project;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;



@RestController
@RequestMapping("/api/v1/projects")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService service;

    @GetMapping
    public List<Project> getAllProjects() {
        return service.findAllProjects();
    }
    @PostMapping
    public ResponseEntity<ProjectDTO> createNewProject(@RequestBody ProjectDTO projectDTO, Principal principal) {
        //TODO: process POST request
        //project.setUser((User)userRepository.findByEmail(principal.getPrincipal()).orElse(null));
        System.out.println(projectDTO.toString());
        ProjectDTO savedProject=service.createNewProject(projectDTO, principal);
        return ResponseEntity.ok(savedProject);
    }
    //Members can view project details, but not update or delete
    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Integer id) {
        return service.findById(id);
    }
    //Owner can update project details, but not members or tasks
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateExistingProject(@RequestBody ProjectDTO projectDTO, @PathVariable Integer id) {
        ProjectDTO updatedProject = service.updateExistingProject(projectDTO, id);
        return ResponseEntity.ok(updatedProject);
    }
    //Owner can delete project, but not members or tasks
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Integer id) {
        service.deleteById(id);
        return ResponseEntity.ok("Project with id " + id + " deleted successfully");
    }

    
    
    
    



}
