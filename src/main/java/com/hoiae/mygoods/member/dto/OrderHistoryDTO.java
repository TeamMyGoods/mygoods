package com.hoiae.mygoods.member.dto;

import java.util.Date;

public class OrderHistoryDTO {
    /*
     * TBL_PRODUCT_ORDER ORDER_CODE: 주문코드 VARCHAR
     * TBL_PRODUCT_ORDER ORDER_DATE: 주문일 DATE
     * TBL_PRODUCT_ORDER ORDER_AMOUNT: 합계금액 INT
     * TBL_PRODUCT_ORDER PRODUCT_SIZE: 상품사이즈 VARCHAR2
     *
     * 조인:PRODUCT_CODE
     *
     * TBL_PRODUCT PRODUCT_NAME: 상품명 STRING
     * TBL_PRODUCT PRODUCT_IMAGE_URL: 상품사진 URL STRING
     * */
    private String orderCode;
    private java.util.Date orderDate;
    private int orderAmount;
    private String productSize;

    private String address;
    private String productName;
    private String productImageUrl;

    public OrderHistoryDTO() {}

    public OrderHistoryDTO(String orderCode, Date orderDate, int orderAmount, String productSize, String address, String productName, String productImageUrl) {
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.orderAmount = orderAmount;
        this.productSize = productSize;
        this.address = address;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    @Override
    public String toString() {
        return "OrderHistoryDTO{" +
                "orderCode='" + orderCode + '\'' +
                ", orderDate=" + orderDate +
                ", orderAmount=" + orderAmount +
                ", productSize='" + productSize + '\'' +
                ", address='" + address + '\'' +
                ", productName='" + productName + '\'' +
                ", productImageUrl='" + productImageUrl + '\'' +
                '}';
    }
}
