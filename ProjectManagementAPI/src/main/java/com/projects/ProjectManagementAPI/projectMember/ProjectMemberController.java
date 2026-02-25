package com.projects.ProjectManagementAPI.projectMember;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
public class ProjectMemberController {
    private final ProjectMemberService service;

    //Members can view project members, but not update or delete
    @GetMapping("/api/v1/projects/{projectId}/members")
    public Set<toProjectMemberInfo> getProjectMembers(@PathVariable Integer projectId) {
        return service.getProjectMembers(projectId);
    }
    //Owner can add project members
    @PostMapping("/api/v1/projects/{projectId}/members")
    public ResponseEntity<String> addProjectMember(@PathVariable Integer projectId, @RequestBody MemberInfoRequest request) {
        return ResponseEntity.ok(service.addProjectMember(projectId, request));
    }
    //Owner can update project member role
    @PutMapping("/api/v1/projects/{projectId}/{userId}/updateRole")
    public ProjectMember updateProjectMember(@PathVariable Integer projectId, @PathVariable Integer userId, @RequestBody ProjectRole role) {
        return service.updateProjectMember(projectId, userId, role);
    }
    //Owner can remove project member
    @DeleteMapping("/api/v1/projects/{projectId}/members/{userId}")
    public void removeProjectMember(@PathVariable Integer projectId, @PathVariable Integer userId) {
        service.removeProjectMember(projectId, userId);
    }

}
