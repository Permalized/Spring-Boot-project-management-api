package com.projects.ProjectManagementAPI.projectMember;

import java.util.Set;

import org.springframework.stereotype.Service;

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


    public Set<toProjectMemberInfo> getProjectMembers(Integer projectId) {
        // return projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId))
        //         .getMembers().stream().map(mapper::toProjectMemberInfo).collect(java.util.stream.Collectors.toSet());
        return repository.findAll().stream()
                .filter(pm -> pm.getId().getProjectId().equals(projectId))
                .map(mapper::toProjectMemberInfo)
                .collect(java.util.stream.Collectors.toSet());
    }


    public String addProjectMember(Integer projectId, MemberInfoRequest request) {
        var userId= userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()))
                .getId();
        ProjectMemberId id = ProjectMemberId.builder()
            .projectId(projectId)
            .userId(userId)
            .build();
        ProjectMember projectMember = 
        ProjectMember.builder()
            .id(id)
            .project(projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId)))
            .user(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId)))
            .role(request.getRole() != null ? request.getRole() : ProjectRole.MEMBER)
            .build();

        repository.save(projectMember);
        var userEmail=request.getEmail();
        return "User with email: " + userEmail + " added to project with id: " + projectId + " as " + projectMember.getRole();
    }


    public ProjectMember updateProjectMember(Integer projectId, Integer userId, ProjectRole role) {
        
        ProjectMemberId id = new ProjectMemberId();
        id.setProjectId(projectId);
        id.setUserId(userId);

        ProjectMember projectMember = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project member not found"));

        projectMember.setRole(role);
        return repository.save(projectMember);
    }


    public void removeProjectMember(Integer projectId, Integer userId) {
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

}
