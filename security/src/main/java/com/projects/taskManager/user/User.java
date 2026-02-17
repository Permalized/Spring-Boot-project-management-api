package com.projects.taskManager.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projects.taskManager.projectMember.ProjectMember;
import com.projects.taskManager.task.Task;
import com.projects.taskManager.token.Token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user") // "user" is a reserved keyword in many SQL databases
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy=jakarta.persistence.GenerationType.AUTO) // Detects the database type and applies the appropriate strategy
  private Integer id;
  private String firstname;
  private String lastname;
  @Column(unique = true, nullable = false)
  private String email;
  
  private String password;

  @Enumerated(EnumType.STRING)
  @NonNull
  private Role role;

  @JsonManagedReference("user-tokens")
  @OneToMany(mappedBy="user")
  private List<Token> tokens;

  @JsonManagedReference("user-tasks")
  @OneToMany(mappedBy="assignedUser")
  private List<Task> tasks;

  @JsonBackReference("user-memberships")
  @OneToMany(mappedBy="user")
  private List<ProjectMember> memberships;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    //return List.of(new SimpleGrantedAuthority(role.name())); 
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
