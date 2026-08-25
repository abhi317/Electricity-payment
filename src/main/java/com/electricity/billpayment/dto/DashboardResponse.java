package com.electricity.billpayment.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        UserResponse user,
        BigDecimal totalDue,
        int totalBills,
        int pendingCount,
        int paidCount,
        List<BillResponse> pendingBills,
        List<PaymentResponse> recentPayments
) {
}
