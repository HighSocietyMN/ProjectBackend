package com.royal.backend.user.service;

import com.royal.backend.user.dto.UserDTO;
import com.royal.backend.user.entity.User;
import com.royal.backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signup(UserDTO userDTO) {
        if (userRepository.findByUserId(userDTO.getUserId()).isPresent()) {
            throw new RuntimeException("User ID already exists");
        }

        User user = User.builder()
                .userId(userDTO.getUserId())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean login(UserDTO userDTO) {
        return userRepository.findByUserId(userDTO.getUserId())
                .map(user -> passwordEncoder.matches(userDTO.getPassword(), user.getPassword()))
                .orElse(false);
    }
}
