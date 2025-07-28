package com.phegondev.InventoryMenagmentSystem.repository;

import com.phegondev.InventoryMenagmentSystem.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
    // Additional query methods can be defined here if needed
}
