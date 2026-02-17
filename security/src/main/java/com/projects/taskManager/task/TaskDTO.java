package com.projects.taskManager.task;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Enumerated;

public record TaskDTO(
    //@NotEmpty 
    @JsonProperty("title_name") String title,
    @JsonProperty("description") String description,
    //@NotNull 
    @Enumerated
    @JsonProperty("status")  StatusType status,
    //@NotNull
    @Enumerated
    @JsonProperty("priority") PriorityType priority,
    LocalDateTime dueDate,
    String assignedUserEmail
) {

}
