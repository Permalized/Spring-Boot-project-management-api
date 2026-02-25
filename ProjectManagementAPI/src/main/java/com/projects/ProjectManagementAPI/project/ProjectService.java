package com.projects.ProjectManagementAPI.project;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.projects.ProjectManagementAPI.config.SecurityConfiguration;
import com.projects.ProjectManagementAPI.exceptions.ResourceNotFoundException;
import com.projects.ProjectManagementAPI.exceptions.UnauthorizedActionException;
import com.projects.ProjectManagementAPI.projectMember.ProjectMember;
import com.projects.ProjectManagementAPI.projectMember.ProjectMemberId;
import com.projects.ProjectManagementAPI.projectMember.ProjectMemberRepository;
import com.projects.ProjectManagementAPI.projectMember.ProjectMemberService;
import com.projects.ProjectManagementAPI.projectMember.ProjectRole;
import com.projects.ProjectManagementAPI.user.UserRepository;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Service
@AllArgsConstructor
public class ProjectService {
    private final SecurityConfiguration securityConfiguration;

    private final ProjectRepository repository;
    private final ProjectMapper mapper;
    private final UserRepository  userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectMemberService projectMemberService;

    public List<Project> findAllProjects() {
        var userId=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new IllegalStateException("User not found in security context"));
        if(projectMemberRepository.findAllByUserId(userId).isEmpty()) {
            throw new ResourceNotFoundException("No projects found for user with id: " + userId);
        }
        else {
            return projectMemberRepository.findAllByUserId(userId).stream().map(ProjectMember::getProject).toList();
        }
    }

    public Project findById(Integer id) {
        var userId=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new IllegalStateException("User not found in security context"));
        if(projectMemberRepository.findById(new ProjectMemberId(id, userId)).isEmpty()) {
            throw new ResourceNotFoundException("Project with id " + id + " not found for user with id: " + userId);
        }
        else {
            return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project with id " + id + " not found"));
        }
    }

    public ProjectDTO createNewProject(ProjectDTO projectDTO, Principal principal) {
        Project savedProject=mapper.toEntity(projectDTO);
        
        var userId=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new IllegalStateException("User not found in security context"));

        ProjectMemberId id = ProjectMemberId.builder()
            .projectId(savedProject.getId())
            .userId(userId)
            .build(); 
        var newProjectMember=ProjectMember.builder()
            .id(id)
            .project(savedProject)
            .user(userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found")))
            .role(ProjectRole.OWNER)
            .build();
        
        savedProject.getMembers().add(newProjectMember);
        repository.save(savedProject); 
        projectMemberRepository.save(newProjectMember);
        return mapper.toDTO(savedProject);
    }

    public ProjectDTO updateExistingProject(ProjectDTO projectDTO, Integer id) {
        var userId=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new IllegalStateException("User not found in security context"));
        if(!projectMemberService.isUserProjectMember(id, userId)) {
            throw new ResourceNotFoundException("Project with id " + id + " not found for user with id: " + userId);
        }
        if(!projectMemberService.isUserOwnerOfProject(id, userId)) {
            throw new UnauthorizedActionException("User with id: " + userId + " is not the owner of project with id: " + id);
        }

        return repository.findById(id).map(project -> {
            project.setName(projectDTO.name());
            project.setDescription(projectDTO.description());
            return mapper.toDTO(repository.save(project));
        }).orElseThrow(() -> new IllegalStateException("Project with id " + id + " not found"));
    }

    public void deleteById(Integer id) {
        
        var userId=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new IllegalStateException("User not found in security context"));
        if(!projectMemberService.isUserProjectMember(id, userId)) {
            throw new ResourceNotFoundException("Project with id " + id + " not found for user with id: " + userId);
        }
        else if(!projectMemberService.isUserOwnerOfProject(id, userId)) {
            throw new UnauthorizedActionException("User with id: " + userId + " is not the owner of project with id: " + id);
        }
        repository.deleteById(id);
    }

 
}
