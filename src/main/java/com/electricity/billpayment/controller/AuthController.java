package com.electricity.billpayment.controller;

import com.electricity.billpayment.dto.RegistrationForm;
import com.electricity.billpayment.dto.UserResponse;
import com.electricity.billpayment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegistrationForm form) {
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (userService.usernameTaken(form.getUsername())) {
            throw new IllegalArgumentException("This username is already taken");
        }
        if (userService.emailTaken(form.getEmail())) {
            throw new IllegalArgumentException("An account with this email already exists");
        }
        if (userService.consumerNumberTaken(form.getConsumerNumber())) {
            throw new IllegalArgumentException("This consumer number is already registered");
        }

        UserResponse response = UserResponse.from(userService.registerUser(form));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
