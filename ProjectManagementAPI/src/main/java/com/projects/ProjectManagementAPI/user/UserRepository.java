package com.projects.ProjectManagementAPI.user;

import java.util.Optional;

public interface UserRepository extends org.springframework.data.jpa.repository.JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    

}
