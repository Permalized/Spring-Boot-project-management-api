package com.projects.taskManager.task;


import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.projects.taskManager.project.Project;
import com.projects.taskManager.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task {

    @Id
    @GeneratedValue(strategy= jakarta.persistence.GenerationType.AUTO)
    private Integer id;
    @Column(nullable = false)
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private StatusType status;
    @Enumerated(EnumType.STRING)
    private PriorityType priority;
    @CreatedDate 
    private LocalDateTime createDate;
    private LocalDateTime dueDate;

    @JsonBackReference(value = "project-tasks")
    @ManyToOne
    @JoinColumn(name = "project_id") // This specifies the foreign key column in the Task table
    private Project project;
    @JsonBackReference(value = "user-tasks")
    @ManyToOne
    @JoinColumn(name = "user_id") // This specifies the foreign key column in the Task table
    private User assignedUser;

    
    

}
