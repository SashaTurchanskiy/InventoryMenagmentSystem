package com.phegondev.InventoryMenagmentSystem.dto;

import com.phegondev.InventoryMenagmentSystem.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    @NotBlank(message = "Password is required")
    private String password;
    private UserRole role; // e.g., "MANAGER", "ADMIN"

}
