package com.projects.ProjectManagementAPI.projectMember;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    Set<ProjectMember> findByProjectId(Integer projectId);

    Set<ProjectMember> findByUserId(Integer userId);
}
