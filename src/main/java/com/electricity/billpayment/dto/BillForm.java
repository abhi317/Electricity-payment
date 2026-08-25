package com.electricity.billpayment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class BillForm {

    @NotBlank(message = "Consumer number is required")
    private String consumerNumber;

    @NotBlank(message = "Billing month is required")
    private String billMonth;

    @NotNull(message = "Units consumed is required")
    @DecimalMin(value = "0.1", message = "Units consumed must be greater than 0")
    private Double unitsConsumed;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    public String getConsumerNumber() {
        return consumerNumber;
    }

    public void setConsumerNumber(String consumerNumber) {
        this.consumerNumber = consumerNumber;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public Double getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(Double unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
