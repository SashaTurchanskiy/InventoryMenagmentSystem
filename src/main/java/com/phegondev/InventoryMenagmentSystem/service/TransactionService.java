package com.phegondev.InventoryMenagmentSystem.service;

import com.phegondev.InventoryMenagmentSystem.dto.CategoryDto;
import com.phegondev.InventoryMenagmentSystem.dto.Response;
import com.phegondev.InventoryMenagmentSystem.dto.TransactionRequest;

public interface TransactionService {

    Response restockInventory(TransactionRequest transactionRequest);

    Response sell(TransactionRequest transactionRequest);

    Response returnToSupplier(TransactionRequest transactionRequest);

    Response getAllTransaction(int page, int size, String searchText);

    Response getTransactionById(Long id);

    Response getAllTransactionByMonthAndYear(int month, int year);

    Response updateTransactionStatus(Long transactionId, TransactionService transactionService);


}
