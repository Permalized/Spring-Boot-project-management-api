package com.projects.taskManager.projectMember;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberId implements java.io.Serializable {

    private Integer projectId; 
    private Integer userId;

}
