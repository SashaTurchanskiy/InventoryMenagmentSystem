package com.phegondev.InventoryMenagmentSystem.security;

import com.phegondev.InventoryMenagmentSystem.entity.User;
import com.phegondev.InventoryMenagmentSystem.exception.NotFoundException;
import com.phegondev.InventoryMenagmentSystem.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username)
                .orElseThrow(()-> new NotFoundException("User not found with email: " + username));
        return AuthUser.builder()
                .user(user)
                .build();

    }
}
