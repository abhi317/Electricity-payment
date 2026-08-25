package com.electricity.billpayment.repository;

import com.electricity.billpayment.model.Bill;
import com.electricity.billpayment.model.BillStatus;
import com.electricity.billpayment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByUserOrderByBillDateDesc(User user);

    List<Bill> findByUserAndStatusOrderByDueDateAsc(User user, BillStatus status);

    Optional<Bill> findByIdAndUser(Long id, User user);

    @Query("select b from Bill b join fetch b.user order by b.billDate desc")
    List<Bill> findAllByOrderByBillDateDesc();
}
