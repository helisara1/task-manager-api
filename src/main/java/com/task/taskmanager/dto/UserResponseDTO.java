package com.task.taskmanager.dto;
import com.task.taskmanager.model.Role;

import lombok.Data;

@Data

public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
    
     public UserResponseDTO(Long id, String username, String email, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
}