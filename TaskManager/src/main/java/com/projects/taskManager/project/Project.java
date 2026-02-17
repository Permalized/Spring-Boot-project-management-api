package com.projects.taskManager.project;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projects.taskManager.projectMember.ProjectMember;
import com.projects.taskManager.task.Task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Data
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Project {
    
    @Id
    @GeneratedValue
    private Integer id;

    @Column()
    private String name;
    
    private String description;

    @JsonManagedReference("project-tasks")
    @OneToMany(mappedBy = "project", cascade = jakarta.persistence.CascadeType.ALL)
    private List<Task> tasks;

    @JsonManagedReference("project-members")
    @OneToMany(mappedBy = "project", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    //@JoinColumn(name = "user_id") // This specifies the foreign key column in the Project table  
    private Set<ProjectMember> members;

    @CreatedDate
    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createDate;

}
