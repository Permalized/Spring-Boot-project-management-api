package com.projects.ProjectManagementAPI.task;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public List<TaskDTO> findAllByProjectId(Integer projectId) {
        return taskRepository.findAllByProjectId(projectId)
        .stream()
        .map(mapper::toDTO).toList();
        
    }

    public Task createTaskForProject(TaskDTO taskDTO, Integer projectId) {
        System.out.println(taskDTO.toString());
        // TODO Auto-generated method stub
        Task savedTask = mapper.toEntity(taskDTO);
        savedTask.setProject(projectRepository.findById(projectId).orElseThrow(() -> new IllegalStateException("Project with id " + projectId + " not found")));
        return taskRepository.save(savedTask);
    }

    public TaskDTO patchTaskStatus(Integer taskId, StatusType status) {
        taskRepository.findById(taskId).orElseThrow(() -> new IllegalStateException("Task with id " + taskId + " not found"));
        taskRepository.findById(taskId).map(task -> {
            task.setStatus(status);
            Task savedTask = taskRepository.save(task);
            return savedTask;
        }).orElseThrow(() -> new IllegalStateException("Task with id " + taskId + " not found"));
        Task savedTask = taskRepository.findById(taskId).orElseThrow(() -> new IllegalStateException("Task with id " + taskId + " not found"));
        return mapper.toDTO(savedTask);
    }

    public ResponseEntity<String> deleteTaskById(Integer taskId) {
        // TODO Auto-generated method stub
        taskRepository.findById(taskId).orElseThrow(() -> new IllegalStateException("Task with id " + taskId + " not found"));
        taskRepository.deleteById(taskId);
        return ResponseEntity.ok("Task with id " + taskId + " deleted successfully");
    }

    public void updateTaskForProject(Integer taskId, TaskDTO taskDTO) {
        var task = taskRepository.findById(taskId).
        orElseThrow(() -> new IllegalStateException("Task with id " + taskId + " not found"));
        task.setTitle(taskDTO.title());
        task.setDescription(taskDTO.description()); 
        task.setStatus(taskDTO.status());
        task.setPriority(taskDTO.priority());
        task.setDueDate(taskDTO.dueDate());
        task.setAssignedUser(userRepository.findByEmail(taskDTO.assignedUserEmail()).orElse(null));
        taskRepository.save(task);
    }

    public TaskDTO assignUserToTask(Integer taskId, AssignUserRequest request) {
        var task = taskRepository.findById(taskId).
        orElseThrow(() -> new IllegalStateException("Task with id " + taskId + " not found"));
        var projectId = task.getProject().getId();
        var userId = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()))
                .getId();
        boolean isUserMember = projectMemberService.isUserProjectMember(projectId, userId);
        if (!isUserMember) {
            throw new RuntimeException("User with email: " + request.getEmail() + " is not a member of the project");
        }
        task.setAssignedUser(userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail())));
        Task savedTask = taskRepository.save(task);
        return mapper.toDTO(savedTask);
    }
    

}
