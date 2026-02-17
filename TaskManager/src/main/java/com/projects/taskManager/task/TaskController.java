package com.projects.taskManager.task;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping
public class TaskController {

    private final TaskService service;
    private final TaskRepository taskRepository;

    @GetMapping("/api/v1/projects/{projectId}/tasks")
    public List<TaskDTO> getTasksForProject(@PathVariable(name = "projectId") Integer projectId) {
        return service.findAllByProjectId(projectId);
    }
    @PostMapping("/api/v1/projects/{projectId}/tasks")
    public ResponseEntity<Task> createTaskForProject(@RequestBody TaskDTO taskDTO, @PathVariable(name = "projectId") Integer projectId) {
        System.out.println(taskDTO.toString());
        Task savedTask = service.createTaskForProject(taskDTO, projectId );
        return ResponseEntity.ok(taskRepository.save(savedTask));
    }
    
    @PutMapping("/api/v1/tasks/{taskId}")
    public String updateTaskForProject(@PathVariable Integer taskId, @RequestBody TaskDTO taskDTO) {
        service.updateTaskForProject(taskId, taskDTO);
        return "Task updated for the project";
    }
    @PatchMapping("/api/v1/tasks/{taskId}/status")
    public ResponseEntity<TaskDTO> patchTaskForProject(@PathVariable Integer taskId, @RequestBody StatusType status) {
        TaskDTO updatedTask = service.patchTaskStatus(taskId, status);
        return ResponseEntity.ok(updatedTask);
    }
    @PatchMapping("/api/v1/tasks/{taskId}/assign")
    public ResponseEntity<TaskDTO> assignUserToTask(@PathVariable Integer taskId, @RequestBody AssignUserRequest request) {
        TaskDTO updatedTask = service.assignUserToTask(taskId, request);
        return ResponseEntity.ok(updatedTask);
        
    }
    @DeleteMapping("/api/v1/tasks/{taskId}")
    public ResponseEntity<?> deleteTaskForProject(@PathVariable Integer taskId) {
        return service.deleteTaskById(taskId);
    }
}
