package com.electricity.billpayment.service;

import com.electricity.billpayment.dto.RegistrationForm;
import com.electricity.billpayment.model.Role;
import com.electricity.billpayment.model.User;
import com.electricity.billpayment.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("No account found for username: " + username));
    }

    public boolean usernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean consumerNumberTaken(String consumerNumber) {
        return userRepository.existsByConsumerNumber(consumerNumber);
    }

    @Transactional
    public User registerUser(RegistrationForm form) {
        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setFullName(form.getFullName());
        user.setEmail(form.getEmail());
        user.setConsumerNumber(form.getConsumerNumber());
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        return userRepository.save(user);
    }
}
