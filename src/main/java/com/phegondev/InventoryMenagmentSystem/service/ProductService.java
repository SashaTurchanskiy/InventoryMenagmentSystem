package com.phegondev.InventoryMenagmentSystem.service;

import com.phegondev.InventoryMenagmentSystem.dto.ProductDto;
import com.phegondev.InventoryMenagmentSystem.dto.Response;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    Response saveProduct(ProductDto productDto, MultipartFile imageFile);

    Response updateProduct(ProductDto productDto, MultipartFile imageFile);

    Response getAllProducts();

    Response getProductById(Long id);

    Response deleteProduct(Long id);
}
