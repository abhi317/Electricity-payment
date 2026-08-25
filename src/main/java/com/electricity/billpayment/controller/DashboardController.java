package com.electricity.billpayment.controller;

import com.electricity.billpayment.dto.BillResponse;
import com.electricity.billpayment.dto.DashboardResponse;
import com.electricity.billpayment.dto.PaymentResponse;
import com.electricity.billpayment.dto.UserResponse;
import com.electricity.billpayment.model.Bill;
import com.electricity.billpayment.model.BillStatus;
import com.electricity.billpayment.model.User;
import com.electricity.billpayment.service.BillService;
import com.electricity.billpayment.service.PaymentService;
import com.electricity.billpayment.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
public class DashboardController {

    private final UserService userService;
    private final BillService billService;
    private final PaymentService paymentService;

    public DashboardController(UserService userService, BillService billService, PaymentService paymentService) {
        this.userService = userService;
        this.billService = billService;
        this.paymentService = paymentService;
    }

    @GetMapping("/api/dashboard")
    public DashboardResponse dashboard(Principal principal) {
        User user = userService.getByUsername(principal.getName());

        List<Bill> pendingBills = billService.getPendingBillsForUser(user);
        List<Bill> allBills = billService.getBillsForUser(user);
        List<PaymentResponse> recentPayments = paymentService.getPaymentsForUser(user).stream()
                .limit(5)
                .map(PaymentResponse::from)
                .toList();

        BigDecimal totalDue = pendingBills.stream()
                .map(Bill::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long paidCount = allBills.stream().filter(b -> b.getStatus() == BillStatus.PAID).count();

        return new DashboardResponse(
                UserResponse.from(user),
                totalDue,
                allBills.size(),
                pendingBills.size(),
                (int) paidCount,
                pendingBills.stream().map(BillResponse::from).toList(),
                recentPayments
        );
    }
}
