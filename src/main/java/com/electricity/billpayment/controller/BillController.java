package com.electricity.billpayment.controller;

import com.electricity.billpayment.dto.BillResponse;
import com.electricity.billpayment.model.User;
import com.electricity.billpayment.service.BillService;
import com.electricity.billpayment.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final UserService userService;
    private final BillService billService;

    public BillController(UserService userService, BillService billService) {
        this.userService = userService;
        this.billService = billService;
    }

    @GetMapping
    public List<BillResponse> listBills(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        return billService.getBillsForUser(user).stream()
                .map(BillResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public BillResponse billDetail(@PathVariable Long id, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        return billService.getBillForUser(id, user)
                .map(BillResponse::from)
                .orElseThrow(() -> new NoSuchElementException("Bill not found"));
    }
}
