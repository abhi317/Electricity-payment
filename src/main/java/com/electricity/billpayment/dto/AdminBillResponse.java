package com.electricity.billpayment.dto;

import com.electricity.billpayment.model.Bill;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AdminBillResponse(
        Long id,
        String consumerNumber,
        String customerName,
        String billMonth,
        double unitsConsumed,
        BigDecimal ratePerUnit,
        BigDecimal amount,
        LocalDate billDate,
        LocalDate dueDate,
        String status,
        boolean overdue
) {
    public static AdminBillResponse from(Bill bill) {
        return new AdminBillResponse(
                bill.getId(),
                bill.getConsumerNumber(),
                bill.getUser().getFullName(),
                bill.getBillMonth(),
                bill.getUnitsConsumed(),
                bill.getRatePerUnit(),
                bill.getAmount(),
                bill.getBillDate(),
                bill.getDueDate(),
                bill.getStatus().name(),
                bill.isOverdue()
        );
    }
}
