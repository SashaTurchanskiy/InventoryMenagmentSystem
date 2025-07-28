package com.phegondev.InventoryMenagmentSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

    @Positive(message = "Product ID is required and must be positive")
    private Long productId;
    @Positive(message = "Quantity is required and must be positive")
    private Integer quantity;

    private Long SupplierId;
    private String description;



}
