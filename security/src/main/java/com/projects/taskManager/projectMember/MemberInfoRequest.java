package com.projects.taskManager.projectMember;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoRequest {

    private String email;
    private ProjectRole role;

}
