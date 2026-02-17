package com.projects.taskManager.projectMember;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projects.taskManager.project.Project;
import com.projects.taskManager.user.User;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember {

    @EmbeddedId
    private ProjectMemberId id;

    @JsonBackReference("project-members")
    @ManyToOne
    @MapsId("projectId") // This tells JPA to use the projectId from the embedded id as the foreign key
    private Project project; 
    @JsonManagedReference("user-memberships")
    @ManyToOne 
    @MapsId("userId") // This tells JPA to use the userId from the embedded id as the foreign key
    private User user;

    @Enumerated(EnumType.STRING)  
    private ProjectRole role;
}
