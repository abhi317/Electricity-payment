package com.electricity.billpayment.dto;

import com.electricity.billpayment.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public class PaymentForm {

    @NotNull(message = "Please select a payment method")
    private PaymentMethod paymentMethod;

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
