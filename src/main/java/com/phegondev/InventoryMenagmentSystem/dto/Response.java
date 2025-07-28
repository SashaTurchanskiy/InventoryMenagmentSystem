package com.phegondev.InventoryMenagmentSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.phegondev.InventoryMenagmentSystem.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    //generic response object for API responses
    private int status;
    private String message;

    // Authentication specific fields
    private String token;
    private UserRole role;
    private String expirationTime;

    // For paginated responses
    private Integer totalPages;
    private Long totalElements;

    // Data objects for various entities
    private UserDto user;
    private List<UserDto> users;

    private SupplierDto supplier;
    private List<SupplierDto> suppliers;

    private CategoryDto category;
    private List<CategoryDto> categories;

    private ProductDto product;
    private List<ProductDto> products;

    private TransactionDto transaction;
    private List<TransactionDto> transactions;

    private final LocalDateTime timestamp = LocalDateTime.now();
}
