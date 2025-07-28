package com.phegondev.InventoryMenagmentSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto {


    private Long id;

    private Long productId;
    private Long categoryId;
    private Long supplierId;

    private String name;
    private String sku;
    private BigDecimal price;
    private Integer sockQuantity;
    private String description;
    private LocalDateTime expiryDate;
    private String imageUrl;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
