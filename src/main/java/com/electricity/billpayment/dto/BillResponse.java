package com.electricity.billpayment.dto;

import com.electricity.billpayment.model.Bill;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BillResponse(
        Long id,
        String consumerNumber,
        String billMonth,
        double unitsConsumed,
        BigDecimal ratePerUnit,
        BigDecimal amount,
        LocalDate billDate,
        LocalDate dueDate,
        String status,
        boolean overdue
) {
    public static BillResponse from(Bill bill) {
        return new BillResponse(
                bill.getId(),
                bill.getConsumerNumber(),
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
