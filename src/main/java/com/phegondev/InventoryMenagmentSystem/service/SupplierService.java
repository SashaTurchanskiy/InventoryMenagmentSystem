package com.phegondev.InventoryMenagmentSystem.service;

import com.phegondev.InventoryMenagmentSystem.dto.Response;
import com.phegondev.InventoryMenagmentSystem.dto.SupplierDto;

public interface SupplierService {

    Response addSupplier(SupplierDto supplierDto);

    Response updateSupplier(Long id, SupplierDto supplierDto);

    Response getAllSuppliers();

    Response getSupplierById(Long id);

    Response deleteSupplier(Long id);
}
