package com.projects.ProjectManagementAPI.projectMember;

public class AuthorizationChecker {
    public static boolean isAuthorized(ProjectRole userRole, ProjectRole requiredRole) {
        if (userRole == ProjectRole.OWNER) {
            return true; // Owners have all permissions
        }
        if (userRole == ProjectRole.MEMBER && requiredRole != ProjectRole.OWNER) {
            return true; // Members can perform actions that don't require OWNER role
        }
        if (userRole == ProjectRole.VIEWER && requiredRole == ProjectRole.VIEWER) {
            return true; // Viewers can only perform actions that require VIEWER role
        }
        return false; // Not authorized for the required role
    }

}
