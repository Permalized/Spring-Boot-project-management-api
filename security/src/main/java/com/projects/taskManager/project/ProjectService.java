package com.projects.taskManager.project;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.projects.taskManager.config.SecurityConfiguration;
import com.projects.taskManager.projectMember.ProjectMember;
import com.projects.taskManager.projectMember.ProjectMemberId;
import com.projects.taskManager.projectMember.ProjectMemberRepository;
import com.projects.taskManager.projectMember.ProjectRole;
import com.projects.taskManager.user.User;
import com.projects.taskManager.user.UserRepository;

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

    public List<Project> findAllProjects() {
        return repository.findAll();
    }

    public Project findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new IllegalStateException("Project with id " + id + " not found"));
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
        return repository.findById(id).map(project -> {
            project.setName(projectDTO.name());
            project.setDescription(projectDTO.description());
            return mapper.toDTO(repository.save(project));
        }).orElseThrow(() -> new IllegalStateException("Project with id " + id + " not found"));
    }

    public void deleteById(Integer id) {
        repository.findById(id).orElseThrow(() -> new IllegalStateException("Project with id " + id + " not found"));
        repository.deleteById(id);
    }

    public Set<ProjectMember> addProjectMember(Project project, User user, ProjectRole role) {
         ProjectMemberId id = ProjectMemberId.builder()
            .projectId(project.getId())
            .userId(user.getId())
            .build();
        ProjectMember newMember = 
        ProjectMember.builder()
            .id(id)
            .project(project)
            .user(user)
            .role(role != null ? role : ProjectRole.MEMBER)
            .build();
        project.getMembers().add(newMember);
        repository.save(project);
        return project.getMembers();

 }
}
