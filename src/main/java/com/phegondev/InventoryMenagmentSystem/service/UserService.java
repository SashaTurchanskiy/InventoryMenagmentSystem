package com.phegondev.InventoryMenagmentSystem.service;

import com.phegondev.InventoryMenagmentSystem.dto.LoginRequest;
import com.phegondev.InventoryMenagmentSystem.dto.RegisterRequest;
import com.phegondev.InventoryMenagmentSystem.dto.Response;
import com.phegondev.InventoryMenagmentSystem.dto.UserDto;
import com.phegondev.InventoryMenagmentSystem.entity.User;

public interface UserService {

    Response registerUser(RegisterRequest registerRequest);

    Response loginUser(LoginRequest loginRequest);

    Response getAllUsers();

    User getCurrentLoggedInUser();

    Response updateUser(Long id, UserDto userDto);

    Response deleteUser(Long id);

    Response getUserTransactions(Long id);

}
