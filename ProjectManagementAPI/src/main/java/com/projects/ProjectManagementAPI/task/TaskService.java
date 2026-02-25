package com.projects.ProjectManagementAPI.task;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.projects.ProjectManagementAPI.config.SecurityConfiguration;
import com.projects.ProjectManagementAPI.exceptions.ResourceNotFoundException;
import com.projects.ProjectManagementAPI.exceptions.UnauthorizedActionException;
import com.projects.ProjectManagementAPI.project.ProjectRepository;
import com.projects.ProjectManagementAPI.projectMember.ProjectMemberService;
import com.projects.ProjectManagementAPI.user.UserRepository;

import lombok.AllArgsConstructor;



@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper mapper;
    private final ProjectRepository projectRepository;
    private final ProjectMemberService projectMemberService;
    private final UserRepository  userRepository;
    private final SecurityConfiguration securityConfiguration;

    public List<TaskDTO> findAllByProjectId(Integer projectId) {
        var userId=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new IllegalStateException("User not found in security context"));
        if (!projectMemberService.isUserProjectMember(projectId, userId)) {
            throw new UnauthorizedActionException("User with id: " + userId + " does not have access to project with id: " + projectId);
        }
        return taskRepository.findAllByProjectId(projectId)
        .stream()
        .map(mapper::toDTO).toList();
        
    }

    public Task createTaskForProject(TaskDTO taskDTO, Integer projectId) {
        var userId=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new IllegalStateException("User not found in security context"));
        if (!projectMemberService.isUserProjectMember(projectId, userId)) {
            throw new RuntimeException("User with id: " + userId + " does not have access to project with id: " + projectId);
        }
        else if (!projectMemberService.isUserOwnerOfProject(projectId, userId)) {
            throw new UnauthorizedActionException("User with id: " + userId + " is not the owner of project with id: " + projectId);
        }
        Task savedTask = mapper.toEntity(taskDTO);
        savedTask.setProject(projectRepository.findById(projectId).orElseThrow(() -> new IllegalStateException("Project with id " + projectId + " not found")));
        return taskRepository.save(savedTask);
    }

    public TaskDTO patchTaskStatus(Integer taskId, StatusType status) {
        var userId=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new ResourceNotFoundException("User not found in security context"));
        var task= taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task with id " + taskId + " not found"));
        var projectId= projectRepository.findById(task.getProject().getId()).orElseThrow(() -> new ResourceNotFoundException("Project with id " + task.getProject().getId() + " not found")).getId();
        if (!projectMemberService.isUserProjectMember(projectId, userId)) {
            throw new UnauthorizedActionException("User with id: " + userId + " does not have access to task with id: " + taskId);
        }
        else if (!(projectMemberService.isUserOwnerOfProject(projectId, userId) || isUserAssignedToTask(taskId, userId))) {
            throw new UnauthorizedActionException("User with id: " + userId + " is not the owner of the project for task with id: " + taskId);
        }
        taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task with id " + taskId + " not found"));
        taskRepository.findById(taskId).map(OldTask -> {
            OldTask.setStatus(status);
            Task savedTask = taskRepository.save(OldTask);
            return savedTask;
        }).orElseThrow(() -> new ResourceNotFoundException("Task with id " + taskId + " not found"));
        Task savedTask = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task with id " + taskId + " not found"));
        return mapper.toDTO(savedTask);
    }

    public ResponseEntity<String> deleteTaskById(Integer taskId) {
        var userId=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new ResourceNotFoundException("User not found in security context"));
        var task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task with id " + taskId + " not found"));
        var projectId = task.getProject().getId();
        if (!projectMemberService.isUserProjectMember(projectId, userId)) {
            throw new UnauthorizedActionException("User with id: " + userId + " does not have access to project with id: " + projectId);
        }
        else if (!projectMemberService.isUserOwnerOfProject(projectId, userId)) {
            throw new UnauthorizedActionException("User with id: " + userId + " is not the owner of project with id: " + projectId);
        }
        taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task with id " + taskId + " not found"));
        taskRepository.deleteById(taskId);
        return ResponseEntity.ok("Task with id " + taskId + " deleted successfully");
    }

    public void updateTaskForProject(Integer taskId, TaskDTO taskDTO) {
        var userId=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new ResourceNotFoundException("User not found in security context"));
        var task= taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task with id " + taskId + " not found"));
        var projectId= projectRepository.findById(task.getProject().getId()).orElseThrow(() -> new ResourceNotFoundException("Project with id " + task.getProject().getId() + " not found")).getId();
        if (!projectMemberService.isUserProjectMember(projectId, userId)) {
            throw new UnauthorizedActionException("User with id: " + userId + " does not have access to task with id: " + taskId);
        }
        else if (!projectMemberService.isUserOwnerOfProject(projectId, userId)) {
            throw new UnauthorizedActionException("User with id: " + userId + " is not the owner of the project for task with id: " + taskId);
        }
        task.setTitle(taskDTO.title());
        task.setDescription(taskDTO.description()); 
        task.setStatus(taskDTO.status());
        task.setPriority(taskDTO.priority());
        task.setDueDate(taskDTO.dueDate());
        task.setAssignedUser(userRepository.findByEmail(taskDTO.assignedUserEmail()).orElse(null));
        taskRepository.save(task);
    }

    public TaskDTO assignUserToTask(Integer taskId, AssignUserRequest request) {
        var userId=securityConfiguration.getUserIdFromSecurityContextHolder().orElseThrow(() -> new ResourceNotFoundException("User not found in security context"));
        var task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task with id " + taskId + " not found"));
        var projectId = task.getProject().getId();
        if (!projectMemberService.isUserProjectMember(projectId, userId)) {
            throw new UnauthorizedActionException("User with id: " + userId + " does not have access to task with id: " + taskId);
        }
        else if (!projectMemberService.isUserOwnerOfProject(projectId, userId)) {
            throw new UnauthorizedActionException("User with id: " + userId + " is not the owner of the project for task with id: " + taskId);
        }
        var userIdToAssign = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()))
                .getId();
        boolean isUserMember = projectMemberService.isUserProjectMember(projectId, userIdToAssign);
        if (!isUserMember) {
            throw new UnauthorizedActionException("User with email: " + request.getEmail() + " is not a member of the project");
        }
        task.setAssignedUser(userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail())));
        Task savedTask = taskRepository.save(task);
        return mapper.toDTO(savedTask);
    }
    
    public boolean isUserAssignedToTask(Integer taskId, Integer userId) {
        var task = taskRepository.findById(taskId).
        orElseThrow(() -> new ResourceNotFoundException("Task with id " + taskId + " not found"));
        return task.getAssignedUser() != null && task.getAssignedUser().getId().equals(userId);
    }
}
