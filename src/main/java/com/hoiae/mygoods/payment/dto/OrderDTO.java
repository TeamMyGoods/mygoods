package com.hoiae.mygoods.payment.dto;

import java.util.Date;

public class OrderDTO {
    private int orderCode;
    private String size;
    private int totalPrice;
    private Date orderDate;
    private String memberCode;
    private String productCode;

    public OrderDTO () {}

    public OrderDTO(int orderCode, String size, int totalPrice, Date orderDate, String memberCode, String productCode) {
        this.orderCode = orderCode;
        this.size = size;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.memberCode = memberCode;
        this.productCode = productCode;
    }

    public int getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(int orderCode) {
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

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderCode=" + orderCode +
                ", size='" + size + '\'' +
                ", totalPrice=" + totalPrice +
                ", orderDate=" + orderDate +
                ", memberCode='" + memberCode + '\'' +
                ", productCode='" + productCode + '\'' +
                '}';
    }
}
