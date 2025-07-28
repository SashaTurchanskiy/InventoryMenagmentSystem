package com.phegondev.InventoryMenagmentSystem.repository;

import com.phegondev.InventoryMenagmentSystem.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
}
