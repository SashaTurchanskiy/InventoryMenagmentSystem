package com.phegondev.InventoryMenagmentSystem.service.impl;

import com.phegondev.InventoryMenagmentSystem.dto.LoginRequest;
import com.phegondev.InventoryMenagmentSystem.dto.RegisterRequest;
import com.phegondev.InventoryMenagmentSystem.dto.Response;
import com.phegondev.InventoryMenagmentSystem.dto.UserDto;
import com.phegondev.InventoryMenagmentSystem.entity.User;
import com.phegondev.InventoryMenagmentSystem.enums.UserRole;
import com.phegondev.InventoryMenagmentSystem.exception.InvalidCredentialsException;
import com.phegondev.InventoryMenagmentSystem.exception.NotFoundException;
import com.phegondev.InventoryMenagmentSystem.repository.UserRepo;
import com.phegondev.InventoryMenagmentSystem.security.JwtUtils;
import com.phegondev.InventoryMenagmentSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;

    @Override
    public Response registerUser(RegisterRequest registerRequest) {
        log.info("Registering user: {}", registerRequest.getName());

        UserRole role = UserRole.MANAGER;

        if (registerRequest.getRole() != null){
            role = registerRequest.getRole();
        }

        User userToSave = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .role(role)
                .build();

        userRepo.save(userToSave);

        return Response.builder()
                .status(200)
                .message("User registered successfully")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user = userRepo.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new NotFoundException("Email not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("password does not match");
        }
        String token = jwtUtils.generateToken(user.getEmail());
        log.info("User {} logged in successfully", user.getName());

        return Response.builder()
                .status(200)
                .message("Login successful")
                .token(token)
                .role(user.getRole())
                .expirationTime("6 months")
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<UserDto> userDTOS = modelMapper.map(users, new TypeToken<List<UserDto>>(){}.getType());
        log.info("Retrieved {} users", userDTOS.size());
        userDTOS.forEach(userDto -> userDto.setTransactions(null));
        return Response.builder()
                .status(200)
                .message("Users retrieved successfully")
                .users(userDTOS)
                .build();

    }

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setTransactions(null); // Clear transactions to avoid circular references

        log.info("Current logged in user: {}", user.getName());
        return user;
    }

    @Override
    public Response updateUser(Long id, UserDto userDto) {

        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (userDto.getEmail() != null) existingUser.setEmail(userDto.getEmail());
        if (userDto.getName() != null) existingUser.setName(userDto.getName());
        if (userDto.getPhoneNumber() != null) existingUser.setPhoneNumber(userDto.getPhoneNumber());
        if (userDto.getRole() != null) existingUser.setRole(userDto.getRole());

        if (userDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        userRepo.save(existingUser);
        log.info("User with ID {} updated successfully", id);

        return Response.builder()
                .status(200)
                .message("User updated successfully")
                .build();
    }

    @Override
    public Response deleteUser(Long id) {

        userRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("User not found"));
        userRepo.deleteById(id);

        log.info("User with ID {} deleted successfully", id);

        return Response.builder()
                .status(200)
                .message("User deleted successfully")
                .build();
    }

    @Override
    public Response getUserTransactions(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.getTransactions().forEach(transactionDto -> {
            transactionDto.setUser(null); // Clear user to avoid circular references
            transactionDto.setSupplier(null);// Clear supplier to avoid circular references;
        });

        log.info("Retrieved transactions for user with ID {}", id);

        return Response.builder()
                .status(200)
                .message("User transactions retrieved successfully")
                .user(userDto)
                .build();
    }
}
