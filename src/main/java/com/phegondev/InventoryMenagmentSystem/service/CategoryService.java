package com.phegondev.InventoryMenagmentSystem.service;

import com.phegondev.InventoryMenagmentSystem.dto.CategoryDto;
import com.phegondev.InventoryMenagmentSystem.dto.Response;

public interface CategoryService {

    Response createCategory(CategoryDto categoryDto);

    Response getAllCategories();

    Response getCategoryById(Long id);

    Response updateCategory(Long id, CategoryDto categoryDto);

    Response deleteCategory(Long id);


}
