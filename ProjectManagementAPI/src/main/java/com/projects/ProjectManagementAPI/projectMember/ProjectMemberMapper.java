package com.projects.ProjectManagementAPI.projectMember;

import org.springframework.stereotype.Service;

@Service
public class ProjectMemberMapper {

    public toProjectMemberInfo toProjectMemberInfo(ProjectMember projectMember) {
        return new toProjectMemberInfo(
            projectMember.getUser().getFirstname(),
            projectMember.getUser().getLastname(),
            projectMember.getUser().getEmail(),
            projectMember.getRole().toString()
        );
    }

}
