package com.electricity.billpayment.service;

import com.electricity.billpayment.dto.BillForm;
import com.electricity.billpayment.model.Bill;
import com.electricity.billpayment.model.BillStatus;
import com.electricity.billpayment.model.User;
import com.electricity.billpayment.repository.BillRepository;
import com.electricity.billpayment.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    private static final BigDecimal RATE_PER_UNIT = new BigDecimal("8.00");

    private final BillRepository billRepository;
    private final UserRepository userRepository;

    public BillService(BillRepository billRepository, UserRepository userRepository) {
        this.billRepository = billRepository;
        this.userRepository = userRepository;
    }

    public List<Bill> getBillsForUser(User user) {
        return billRepository.findByUserOrderByBillDateDesc(user);
    }

    public List<Bill> getPendingBillsForUser(User user) {
        return billRepository.findByUserAndStatusOrderByDueDateAsc(user, BillStatus.PENDING);
    }

    public Optional<Bill> getBillForUser(Long id, User user) {
        return billRepository.findByIdAndUser(id, user);
    }

    public List<Bill> getAllBills() {
        return billRepository.findAllByOrderByBillDateDesc();
    }

    @Transactional
    public Bill createBill(BillForm form) {
        User user = userRepository.findByConsumerNumber(form.getConsumerNumber())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No customer found with consumer number: " + form.getConsumerNumber()));

        BigDecimal amount = RATE_PER_UNIT
                .multiply(BigDecimal.valueOf(form.getUnitsConsumed()))
                .setScale(2, RoundingMode.HALF_UP);

        Bill bill = new Bill();
        bill.setUser(user);
        bill.setConsumerNumber(user.getConsumerNumber());
        bill.setBillMonth(form.getBillMonth());
        bill.setUnitsConsumed(form.getUnitsConsumed());
        bill.setRatePerUnit(RATE_PER_UNIT);
        bill.setAmount(amount);
        bill.setBillDate(LocalDate.now());
        bill.setDueDate(form.getDueDate());
        bill.setStatus(BillStatus.PENDING);

        return billRepository.save(bill);
    }
}
