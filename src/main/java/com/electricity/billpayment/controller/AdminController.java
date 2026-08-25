package com.electricity.billpayment.controller;

import com.electricity.billpayment.dto.AdminBillResponse;
import com.electricity.billpayment.dto.BillForm;
import com.electricity.billpayment.service.BillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/bills")
public class AdminController {

    private final BillService billService;

    public AdminController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public List<AdminBillResponse> listBills() {
        return billService.getAllBills().stream()
                .map(AdminBillResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<AdminBillResponse> createBill(@Valid @RequestBody BillForm form) {
        AdminBillResponse response = AdminBillResponse.from(billService.createBill(form));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
