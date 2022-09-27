package com.hoiae.mygoods.payment.dto;

public class PaymentDTO {
    private String paymentCode;
    private String isPaid;
    private String orderCode;
    private String isRefunded;

    public PaymentDTO(){}

    public PaymentDTO(String paymentCode, String isPaid, String orderCode, String isRefunded) {
        this.paymentCode = paymentCode;
        this.isPaid = isPaid;
        this.orderCode = orderCode;
        this.isRefunded = isRefunded;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getIsRefunded() {
        return isRefunded;
    }

    public void setIsRefunded(String isRefunded) {
        this.isRefunded = isRefunded;
    }

    @Override
    public String toString() {
        return "PaymentDTO{" +
                "paymentCode='" + paymentCode + '\'' +
                ", isPaid='" + isPaid + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", isRefunded='" + isRefunded + '\'' +
                '}';
    }
}
