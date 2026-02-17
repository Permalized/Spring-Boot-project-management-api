package com.projects.taskManager.project;

import java.util.HashSet;

import org.springframework.stereotype.Service;


@Service
public class ProjectMapper {

    public ProjectDTO toDTO(Project project) {
        return new ProjectDTO(project.getId(), project.getName(), project.getDescription());
    }

    public Project toEntity(ProjectDTO projectDTO) {
        return Project.builder()
                .name(projectDTO.name())
                .description(projectDTO.description())
                .members(new HashSet<>()) // Initialize members as an empty set
                .build();
    }

}
