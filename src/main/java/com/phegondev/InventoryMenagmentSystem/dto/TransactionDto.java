package com.phegondev.InventoryMenagmentSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.phegondev.InventoryMenagmentSystem.enums.TransactionStatus;
import com.phegondev.InventoryMenagmentSystem.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {


    private Long id;

    private Integer totalProducts;

    private BigDecimal totalPrice;

    private TransactionType transactionType;

    private TransactionStatus status;

    private String description;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private UserDto user;
    private ProductDto product;
    private SupplierDto supplier;
}
