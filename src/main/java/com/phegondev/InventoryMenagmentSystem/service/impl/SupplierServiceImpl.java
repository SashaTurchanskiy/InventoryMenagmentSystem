package com.phegondev.InventoryMenagmentSystem.service.impl;

import com.phegondev.InventoryMenagmentSystem.dto.Response;
import com.phegondev.InventoryMenagmentSystem.dto.SupplierDto;
import com.phegondev.InventoryMenagmentSystem.entity.Supplier;
import com.phegondev.InventoryMenagmentSystem.exception.NotFoundException;
import com.phegondev.InventoryMenagmentSystem.repository.SupplierRepo;
import com.phegondev.InventoryMenagmentSystem.service.SupplierService;
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
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepo supplierRepo;
    private final ModelMapper modelMapper;

    @Override
    public Response addSupplier(SupplierDto supplierDto) {
        Supplier supplierToSave = modelMapper.map(supplierDto, Supplier.class);
        supplierRepo.save(supplierToSave);
        log.info("Supplier {} created successfully", supplierToSave.getName());
        return Response.builder()
                .status(200)
                .message("Supplier created successfully")
                .build();
    }

    @Override
    public Response updateSupplier(Long id, SupplierDto supplierDto) {
        Supplier existingSupplier = supplierRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Supplier not found with id: " + id));

       if (supplierDto.getName() != null) existingSupplier.setName(supplierDto.getName());
       if (supplierDto.getAddress() != null) existingSupplier.setAddress(supplierDto.getAddress());

       supplierRepo.save(existingSupplier);

        log.info("Supplier with id {} updated successfully", id);
        return Response.builder()
                .status(200)
                .message("Supplier updated successfully")
                .build();
    }

    @Override
    public Response getAllSuppliers() {
        List<Supplier> suppliers = supplierRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<SupplierDto> supplierDTOS = modelMapper.map(suppliers, new TypeToken<List<SupplierDto>>(){}.getType());

        log.info("Retrieved {} suppliers successfully", supplierDTOS.size());

        return Response.builder()
                .status(200)
                .message("Suppliers retrieved successfully")
                .suppliers(supplierDTOS)
                .build();
    }

    @Override
    public Response getSupplierById(Long id) {
        Supplier supplier = supplierRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Supplier not found with id: " + id));

        SupplierDto supplierDto = modelMapper.map(supplier, SupplierDto.class);
        log.info("Supplier with id {} retrieved successfully", id);
        return Response.builder()
                .status(200)
                .message("Supplier retrieved successfully")
                .supplier(supplierDto)
                .build();
    }

    @Override
    public Response deleteSupplier(Long id) {
        Supplier supplier = supplierRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Supplier not found with id: " + id));
        supplierRepo.delete(supplier);
        return Response.builder()
                .status(200)
                .message("Supplier deleted successfully")
                .build();
    }
}
