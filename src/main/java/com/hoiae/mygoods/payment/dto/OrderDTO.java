package com.hoiae.mygoods.payment.dto;

import java.util.Date;

public class OrderDTO {
    private String orderCode;
    private String size;
    private int totalPrice;
    private Date orderDate;
    private int memberCode;
    private int productCode;

    public OrderDTO () {}

    public OrderDTO(String orderCode, String size, int totalPrice, Date orderDate, int memberCode, int productCode) {
        this.orderCode = orderCode;
        this.size = size;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.memberCode = memberCode;
        this.productCode = productCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(int memberCode) {
        this.memberCode = memberCode;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderCode='" + orderCode + '\'' +
                ", size='" + size + '\'' +
                ", totalPrice=" + totalPrice +
                ", orderDate=" + orderDate +
                ", memberCode=" + memberCode +
                ", productCode=" + productCode +
                '}';
    }
}
