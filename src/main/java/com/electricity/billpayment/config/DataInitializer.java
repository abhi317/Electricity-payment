package com.electricity.billpayment.config;

import com.electricity.billpayment.model.*;
import com.electricity.billpayment.repository.BillRepository;
import com.electricity.billpayment.repository.PaymentRepository;
import com.electricity.billpayment.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Seeds an admin account and a demo customer with sample bills on first run,
 * so the app can be explored immediately against an empty database.
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedDatabase(UserRepository userRepository,
                                           BillRepository billRepository,
                                           PaymentRepository paymentRepository,
                                           PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setEmail("admin@electricityboard.example");
            admin.setConsumerNumber("ADMIN-0000");
            admin.setRole(Role.ROLE_ADMIN);
            userRepository.save(admin);

            User demoUser = new User();
            demoUser.setUsername("john");
            demoUser.setPassword(passwordEncoder.encode("john123"));
            demoUser.setFullName("John Doe");
            demoUser.setEmail("john.doe@example.com");
            demoUser.setConsumerNumber("CN-10001");
            demoUser.setRole(Role.ROLE_USER);
            userRepository.save(demoUser);

            BigDecimal rate = new BigDecimal("8.00");

            Bill pendingBill = new Bill();
            pendingBill.setUser(demoUser);
            pendingBill.setConsumerNumber(demoUser.getConsumerNumber());
            pendingBill.setBillMonth("2026-08");
            pendingBill.setUnitsConsumed(245);
            pendingBill.setRatePerUnit(rate);
            pendingBill.setAmount(rate.multiply(BigDecimal.valueOf(245)));
            pendingBill.setBillDate(LocalDate.now().minusDays(5));
            pendingBill.setDueDate(LocalDate.now().plusDays(10));
            pendingBill.setStatus(BillStatus.PENDING);
            billRepository.save(pendingBill);

            Bill paidBill = new Bill();
            paidBill.setUser(demoUser);
            paidBill.setConsumerNumber(demoUser.getConsumerNumber());
            paidBill.setBillMonth("2026-07");
            paidBill.setUnitsConsumed(210);
            paidBill.setRatePerUnit(rate);
            paidBill.setAmount(rate.multiply(BigDecimal.valueOf(210)));
            paidBill.setBillDate(LocalDate.now().minusMonths(1).minusDays(5));
            paidBill.setDueDate(LocalDate.now().minusDays(20));
            paidBill.setStatus(BillStatus.PAID);
            billRepository.save(paidBill);

            Payment payment = new Payment();
            payment.setBill(paidBill);
            payment.setUser(demoUser);
            payment.setAmount(paidBill.getAmount());
            payment.setPaymentMethod(PaymentMethod.UPI);
            payment.setTransactionId("TXN-DEMO0000001");
            payment.setPaymentDate(LocalDateTime.now().minusDays(19));
            payment.setStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);
        };
    }
}
