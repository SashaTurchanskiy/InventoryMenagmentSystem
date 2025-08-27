package com.phegondev.InventoryMenagmentSystem.service.impl;

import com.phegondev.InventoryMenagmentSystem.dto.Response;
import com.phegondev.InventoryMenagmentSystem.dto.TransactionDto;
import com.phegondev.InventoryMenagmentSystem.dto.TransactionRequest;
import com.phegondev.InventoryMenagmentSystem.entity.Product;
import com.phegondev.InventoryMenagmentSystem.entity.Supplier;
import com.phegondev.InventoryMenagmentSystem.entity.Transaction;
import com.phegondev.InventoryMenagmentSystem.entity.User;
import com.phegondev.InventoryMenagmentSystem.enums.TransactionStatus;
import com.phegondev.InventoryMenagmentSystem.enums.TransactionType;
import com.phegondev.InventoryMenagmentSystem.exception.NameValueRequiredException;
import com.phegondev.InventoryMenagmentSystem.exception.NotFoundException;
import com.phegondev.InventoryMenagmentSystem.repository.ProductRepo;
import com.phegondev.InventoryMenagmentSystem.repository.SupplierRepo;
import com.phegondev.InventoryMenagmentSystem.repository.TransactionRepo;
import com.phegondev.InventoryMenagmentSystem.service.TransactionService;
import com.phegondev.InventoryMenagmentSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;
    private final ModelMapper modelMapper;
    private final SupplierRepo supplierRepo;
    private final UserService userService;
    private final ProductRepo productRepo;

    @Override
    public Response restockInventory(TransactionRequest transactionRequest) {

        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if (supplierId == null) throw new NameValueRequiredException("Supplier ID is required");

        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product with ID " + productId + " not found"));

        Supplier supplier = supplierRepo.findById(supplierId)
                .orElseThrow(()-> new NotFoundException("Supplier with ID " + supplierId + " not found"));

        User user = userService.getCurrentLoggedInUser();

        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() + quantity);

        log.info("Updated stock quantity for product ID {}: new quantity is {}", productId, product.getStockQuantity());

        productRepo.save(product);

        //create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .build();

        transactionRepo.save(transaction);

        return Response.builder()
                .status(200)
                .message("Inventory restocked successfully")
                .build();
    }

    @Override
    public Response sell(TransactionRequest transactionRequest) {

        Long productId = transactionRequest.getProductId();
        Integer quantity = transactionRequest.getQuantity();

        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product with ID " + productId + " not found"));

        User user = userService.getCurrentLoggedInUser();

        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepo.save(product);

        //create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.SALE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .build();

        transactionRepo.save(transaction);

        return Response.builder()
                .status(200)
                .message("Product sold successfully")
                .build();

    }

    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {

        Long productId = transactionRequest.getProductId();
        Long supplierId = transactionRequest.getSupplierId();
        Integer quantity = transactionRequest.getQuantity();

        if (supplierId == null) throw new NameValueRequiredException("Supplier ID is required");

        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product with ID " + productId + " not found"));

        Supplier supplier = supplierRepo.findById(supplierId)
                .orElseThrow(()-> new NotFoundException("Supplier with ID " + supplierId + " not found"));

        User user = userService.getCurrentLoggedInUser();

        //update the stock quantity and re-save
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepo.save(product);

        //create a transaction
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .status(TransactionStatus.PROCESSING)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .description(transactionRequest.getDescription())
                .build();

        transactionRepo.save(transaction);

        return Response.builder()
                .status(200)
                .message("Product return to supplier initiated successfully")
                .build();
    }

    @Override
    public Response getAllTransaction(int page, int size, String searchText) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Transaction> transactionPage = transactionRepo.searchTransactions(searchText, pageable);

        List<TransactionDto> transactionDTOS = modelMapper
                .map(transactionPage.getContent(), new TypeToken<List<TransactionDto>>() {}.getType());

        transactionDTOS.forEach(transactionDtoItem ->{
            transactionDtoItem.setUser(null);
            transactionDtoItem.setProduct(null);
            transactionDtoItem.setSupplier(null);
        } );

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .build();
    }

    @Override
    public Response getTransactionById(Long id) {
        Transaction transaction = transactionRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("Transaction with ID " + id + " not found"));
        TransactionDto transactionDto = modelMapper.map(transaction, TransactionDto.class);
        transactionDto.getUser().setTransactions(null);
        log.info("Fetched transaction details for ID {}: {}", id, transactionDto);
        return Response.builder()
                .status(200)
                .message("success")
                .transaction(transactionDto)
                .build();
    }

    @Override
    public Response getAllTransactionByMonthAndYear(int month, int year) {

        List<Transaction> transactions = transactionRepo.findByMonthAndYear(month, year);
        List<TransactionDto> transactionDTOS = modelMapper
                .map(transactions, new TypeToken<List<TransactionDto>>() {}.getType());

        transactionDTOS.forEach(transactionDtoItem ->{
            transactionDtoItem.setUser(null);
            transactionDtoItem.setProduct(null);
            transactionDtoItem.setSupplier(null);
        } );

        log.info("Fetched {} transactions for {}/{}", transactionDTOS.size(), month, year);

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDTOS)
                .build();
    }

    @Override
    public Response updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus) {
        Transaction existingTransaction = transactionRepo.findById(transactionId)
                .orElseThrow(()-> new NotFoundException("Transaction with ID " + transactionId + " not found"));
        existingTransaction.setStatus(transactionStatus);
        existingTransaction.setUpdatedAt(LocalDateTime.now());
        transactionRepo.save(existingTransaction);

        log.info("Updated transaction ID {} status to {}", transactionId, transactionStatus);

        return Response.builder()
                .status(200)
                .message("Transaction status updated successfully")
                .build();
    }
}
