package com.hoiae.mygoods.product.dto;

public class ProductDTO {
    private int productCode;
    private String productName;
    private int productPrice;
    private int categoryCode;
    private int charachterCode;
    private String productImageUrl;


    public ProductDTO() {
    }

    public ProductDTO(int productCode, String productName, int productPrice, int categoryCode, int charachterCode, String productImageUrl) {
        this.productCode = productCode;
        this.productName = productName;
        this.productPrice = productPrice;
        this.categoryCode = categoryCode;
        this.charachterCode = charachterCode;
        this.productImageUrl = productImageUrl;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public int getCharachterCode() {
        return charachterCode;
    }

    public void setCharachterCode(int charachterCode) {
        this.charachterCode = charachterCode;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }



    @Override
    public String toString() {
        return "ProductDTO{" +
                "productCode=" + productCode +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", categoryCode=" + categoryCode +
                ", charachterCode=" + charachterCode +
                ", productImageUrl='" + productImageUrl + '\'' +
                '}';
    }


}
