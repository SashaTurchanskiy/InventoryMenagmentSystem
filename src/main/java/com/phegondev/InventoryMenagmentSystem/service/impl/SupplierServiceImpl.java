package com.phegondev.InventoryMenagmentSystem.service.impl;

import com.phegondev.InventoryMenagmentSystem.dto.Response;
import com.phegondev.InventoryMenagmentSystem.dto.SupplierDto;
import com.phegondev.InventoryMenagmentSystem.repository.SupplierRepo;
import com.phegondev.InventoryMenagmentSystem.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepo supplierRepo;

    @Override
    public Response addSupplier(SupplierDto supplierDto) {
        return null;
    }

    @Override
    public Response updateSupplier(Long id, SupplierDto supplierDto) {
        return null;
    }

    @Override
    public Response getAllSuppliers() {
        return null;
    }

    @Override
    public Response getSupplierById(Long id) {
        return null;
    }

    @Override
    public Response deleteSupplier(Long id) {
        return null;
    }
}
