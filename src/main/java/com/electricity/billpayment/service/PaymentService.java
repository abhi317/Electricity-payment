package com.electricity.billpayment.service;

import com.electricity.billpayment.model.Bill;
import com.electricity.billpayment.model.BillStatus;
import com.electricity.billpayment.model.Payment;
import com.electricity.billpayment.model.PaymentMethod;
import com.electricity.billpayment.model.PaymentStatus;
import com.electricity.billpayment.model.User;
import com.electricity.billpayment.repository.BillRepository;
import com.electricity.billpayment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;

    public PaymentService(PaymentRepository paymentRepository, BillRepository billRepository) {
        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;
    }

    @Transactional
    public Payment payBill(Bill bill, User user, PaymentMethod method) {
        if (bill.getStatus() == BillStatus.PAID) {
            throw new IllegalStateException("This bill has already been paid.");
        }

        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setUser(user);
        payment.setAmount(bill.getAmount());
        payment.setPaymentMethod(method);
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.SUCCESS);

        bill.setStatus(BillStatus.PAID);
        billRepository.save(bill);

        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsForUser(User user) {
        return paymentRepository.findByUserOrderByPaymentDateDesc(user);
    }

    public Optional<Payment> getPaymentForUser(Long id, User user) {
        return paymentRepository.findByIdAndUser(id, user);
    }
}
