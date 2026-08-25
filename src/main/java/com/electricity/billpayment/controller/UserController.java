package com.electricity.billpayment.controller;

import com.electricity.billpayment.dto.UserResponse;
import com.electricity.billpayment.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse me(Principal principal) {
        return UserResponse.from(userService.getByUsername(principal.getName()));
    }
}
