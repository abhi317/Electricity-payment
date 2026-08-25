package com.electricity.billpayment.repository;

import com.electricity.billpayment.model.Bill;
import com.electricity.billpayment.model.Payment;
import com.electricity.billpayment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select p from Payment p join fetch p.bill where p.user = ?1 order by p.paymentDate desc")
    List<Payment> findByUserOrderByPaymentDateDesc(User user);

    Optional<Payment> findByBill(Bill bill);

    Optional<Payment> findByIdAndUser(Long id, User user);
}
