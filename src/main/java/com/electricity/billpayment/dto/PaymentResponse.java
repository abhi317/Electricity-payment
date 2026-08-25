package com.electricity.billpayment.dto;

import com.electricity.billpayment.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long billId,
        String billMonth,
        BigDecimal amount,
        String paymentMethod,
        String transactionId,
        LocalDateTime paymentDate,
        String status
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getBill().getId(),
                payment.getBill().getBillMonth(),
                payment.getAmount(),
                payment.getPaymentMethod().name(),
                payment.getTransactionId(),
                payment.getPaymentDate(),
                payment.getStatus().name()
        );
    }
}
