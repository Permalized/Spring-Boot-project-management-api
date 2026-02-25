package com.projects.ProjectManagementAPI.projectMember;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.projects.ProjectManagementAPI.config.SecurityConfiguration;
import com.projects.ProjectManagementAPI.project.ProjectRepository;
import com.projects.ProjectManagementAPI.user.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProjectMemberService {

    private final ProjectMemberMapper mapper;
    private final ProjectMemberRepository repository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final SecurityConfiguration securityConfiguration;


    public Set<toProjectMemberInfo> getProjectMembers(Integer projectId) {
        var userId=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new IllegalStateException("User not found in security context"));
        if (!isUserProjectMember(projectId, userId)) {
            throw new RuntimeException("User with id: " + userId + " does not have access to project with id: " + projectId);
        }
        return repository.findAll().stream()
                .filter(pm -> pm.getId().getProjectId().equals(projectId))
                .map(mapper::toProjectMemberInfo)
                .collect(java.util.stream.Collectors.toSet());
    }

    public String addProjectMember(Integer projectId, MemberInfoRequest request) {
        var userId=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new IllegalStateException("User not found in security context"));
        if (!isUserOwnerOfProject(projectId, userId)) {
            throw new RuntimeException("User with id: " + userId + " is not the owner of project with id: " + projectId);
        }
        var userIdToAdd= userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()))
                .getId();
        ProjectMemberId id = ProjectMemberId.builder()
            .projectId(projectId)
            .userId(userIdToAdd)
            .build();
        ProjectMember projectMember = 
        ProjectMember.builder()
            .id(id)
            .project(projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId)))
            .user(userRepository.findById(userIdToAdd).orElseThrow(() -> new RuntimeException("User not found with id: " + userIdToAdd)))
            .role(request.getRole() != null ? request.getRole() : ProjectRole.MEMBER)
            .build();

        repository.save(projectMember);
        var userEmail=request.getEmail();
        return "User with email: " + userEmail + " added to project with id: " + projectId + " as " + projectMember.getRole();
    }


    public ProjectMember updateProjectMember(Integer projectId, Integer userId, ProjectRole role) {
        var userIdFromContext=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new IllegalStateException("User not found in security context"));
        if (!isUserOwnerOfProject(projectId, userIdFromContext)) {
            throw new RuntimeException("User with id: " + userIdFromContext + " is not the owner of project with id: " + projectId);
        }
        ProjectMemberId id = ProjectMemberId.builder()
                .projectId(projectId)
                .userId(userId)
                .build();


        ProjectMember projectMember = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project member not found"));

        projectMember.setRole(role);
        return repository.save(projectMember);
    }


    public void removeProjectMember(Integer projectId, Integer userId) {
        var userIdFromContext=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new IllegalStateException("User not found in security context"));
        if (!isUserOwnerOfProject(projectId, userIdFromContext)) {
            throw new RuntimeException("User with id: " + userIdFromContext + " is not the owner of project with id: " + projectId);
        }
        ProjectMemberId id = ProjectMemberId.builder()
                .projectId(projectId)
                .userId(userId)
                .build();
        repository.deleteById(id);
    }

    public boolean isUserProjectMember(Integer projectId, Integer userId) {
        ProjectMemberId id = ProjectMemberId.builder()
                .projectId(projectId)
                .userId(userId)
                .build();
        return repository.existsById(id);
    }
    public boolean isUserOwnerOfProject(Integer projectId, Integer userId) {
        ProjectMemberId id = ProjectMemberId.builder()
                .projectId(projectId)
                .userId(userId)
                .build();
        return repository.findById(id).map(ProjectMember::getRole).orElse(null) == ProjectRole.OWNER;
    }
}
