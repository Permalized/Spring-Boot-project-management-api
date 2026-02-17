package com.projects.taskManager.task;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignUserRequest {

    private String email;

}
