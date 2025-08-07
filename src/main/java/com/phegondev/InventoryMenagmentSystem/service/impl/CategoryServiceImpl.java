package com.phegondev.InventoryMenagmentSystem.service.impl;

import com.phegondev.InventoryMenagmentSystem.dto.CategoryDto;
import com.phegondev.InventoryMenagmentSystem.dto.Response;
import com.phegondev.InventoryMenagmentSystem.entity.Category;
import com.phegondev.InventoryMenagmentSystem.exception.NotFoundException;
import com.phegondev.InventoryMenagmentSystem.repository.CategoryRepo;
import com.phegondev.InventoryMenagmentSystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    @Override
    public Response createCategory(CategoryDto categoryDto) {
        Category categoryToSave = modelMapper.map(categoryDto, Category.class);
        categoryRepo.save(categoryToSave);

        log.info("Category {} created successfully", categoryToSave.getName());

        return Response.builder()
                .status(200)
                .message("Category created successfully")
                .build();
    }

    @Override
    public Response getAllCategories() {
        List<Category> categories = categoryRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<CategoryDto> categoryDTOS = modelMapper.map(categories, new TypeToken<List<CategoryDto>>(){}.getType());

        log.info("Retrieved {} categories successfully", categoryDTOS.size());

        return Response.builder()
                .status(200)
                .message("Categories retrieved successfully")
                .categories(categoryDTOS)
                .build();
    }

    @Override
    public Response getCategoryById(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Category not found with id: " + id));
        CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);

        log.info("Category with id {} retrieved successfully", id);

        return Response.builder()
                .status(200)
                .message("Category retrieved successfully")
                .category(categoryDto)
                .build();
    }

    @Override
    public Response updateCategory(Long id, CategoryDto categoryDto) {
        Category existingCategory = categoryRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Category not found with id: " + id));

        existingCategory.setName(categoryDto.getName());
        categoryRepo.save(existingCategory);

        log.info("Category with id {} updated successfully", id);

        return Response.builder()
                .status(200)
                .message("Category updated successfully")
                .build();
    }

    @Override
    public Response deleteCategory(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Category not found with id: " + id));
        categoryRepo.deleteById(id);

        log.info("Category with id {} deleted successfully", id);

        return Response.builder()
                .status(200)
                .message("Category deleted successfully")
                .build();
    }
}
