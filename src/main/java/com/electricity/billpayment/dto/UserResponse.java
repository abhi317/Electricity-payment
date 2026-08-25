package com.electricity.billpayment.dto;

import com.electricity.billpayment.model.User;

public record UserResponse(
        Long id,
        String username,
        String fullName,
        String email,
        String consumerNumber,
        String role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getConsumerNumber(),
                user.getRole().name()
        );
    }
}
