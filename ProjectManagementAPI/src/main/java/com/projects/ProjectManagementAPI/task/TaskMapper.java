package com.projects.ProjectManagementAPI.task;

import org.springframework.stereotype.Service;

import com.projects.ProjectManagementAPI.user.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaskMapper {
    private final UserRepository userRepository;

    public TaskDTO toDTO(Task task) {
        return new TaskDTO(task.getTitle(),
        task.getDescription(),
        task.getStatus(),
        task.getPriority(), 
        task.getDueDate(),
        task.getAssignedUser() != null ? task.getAssignedUser().getEmail() : null);
    }

    public Task toEntity(TaskDTO taskDTO) {
        return Task.builder()
                .title(taskDTO.title())
                .description(taskDTO.description())
                .status(taskDTO.status())
                .priority(taskDTO.priority())
                .dueDate(taskDTO.dueDate())
                .assignedUser(userRepository.findByEmail(taskDTO.assignedUserEmail()).orElse(null))
                .build();
    }

}
