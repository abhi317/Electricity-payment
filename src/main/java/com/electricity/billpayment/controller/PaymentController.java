package com.electricity.billpayment.controller;

import com.electricity.billpayment.dto.PaymentForm;
import com.electricity.billpayment.dto.PaymentResponse;
import com.electricity.billpayment.model.Bill;
import com.electricity.billpayment.model.User;
import com.electricity.billpayment.service.BillService;
import com.electricity.billpayment.service.PaymentService;
import com.electricity.billpayment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class PaymentController {

    private final UserService userService;
    private final BillService billService;
    private final PaymentService paymentService;

    public PaymentController(UserService userService, BillService billService, PaymentService paymentService) {
        this.userService = userService;
        this.billService = billService;
        this.paymentService = paymentService;
    }

    @PostMapping("/api/bills/{id}/pay")
    public ResponseEntity<PaymentResponse> pay(@PathVariable Long id,
                                                @Valid @RequestBody PaymentForm form,
                                                Principal principal) {
        User user = userService.getByUsername(principal.getName());
        Bill bill = billService.getBillForUser(id, user)
                .orElseThrow(() -> new NoSuchElementException("Bill not found"));

        PaymentResponse response = PaymentResponse.from(
                paymentService.payBill(bill, user, form.getPaymentMethod()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/payments")
    public List<PaymentResponse> history(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        return paymentService.getPaymentsForUser(user).stream()
                .map(PaymentResponse::from)
                .toList();
    }

    @GetMapping("/api/payments/{id}")
    public PaymentResponse detail(@PathVariable Long id, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        return paymentService.getPaymentForUser(id, user)
                .map(PaymentResponse::from)
                .orElseThrow(() -> new NoSuchElementException("Payment record not found"));
    }
}
