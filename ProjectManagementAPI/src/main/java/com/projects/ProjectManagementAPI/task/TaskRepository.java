package com.projects.ProjectManagementAPI.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    public List<Task> findAllByProjectId(Integer projectId);

}
