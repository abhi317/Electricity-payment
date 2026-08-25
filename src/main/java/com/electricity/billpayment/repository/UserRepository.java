package com.electricity.billpayment.repository;

import com.electricity.billpayment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByConsumerNumber(String consumerNumber);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByConsumerNumber(String consumerNumber);
}
