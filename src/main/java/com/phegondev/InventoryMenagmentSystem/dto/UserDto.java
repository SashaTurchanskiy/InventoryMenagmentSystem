package com.phegondev.InventoryMenagmentSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.phegondev.InventoryMenagmentSystem.enums.UserRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {


    private Long id;

    private String name;

    private String email;

    @JsonIgnore
    private String password;

    private String phoneNumber;

    private List<TransactionDto> transactions;

    private UserRole role;

    private LocalDateTime createdAt;
}
