package com.hoiae.mygoods.member.dto;

import java.util.Date;

public class FindOrderDTO {


    private String orderCode;
    private int orderAmount;
    private java.util.Date orderDate;
    private String productSize;
    private int productCode;

    public FindOrderDTO() {}

    public FindOrderDTO(String orderCode, int orderAmount, Date orderDate, String productSize, int productCode) {
        this.orderCode = orderCode;
        this.orderAmount = orderAmount;
        this.orderDate = orderDate;
        this.productSize = productSize;
        this.productCode = productCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    @Override
    public String toString() {
        return "FindOrderDTO{" +
                "orderCode=" + orderCode +
                ", orderAmount=" + orderAmount +
                ", orderDate=" + orderDate +
                ", productSize='" + productSize + '\'' +
                ", productCode=" + productCode +
                '}';
    }
}
