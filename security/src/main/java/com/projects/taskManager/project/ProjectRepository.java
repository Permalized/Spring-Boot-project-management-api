package com.projects.taskManager.project;

import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends org.springframework.data.jpa.repository.JpaRepository<Project, Integer> {

}
