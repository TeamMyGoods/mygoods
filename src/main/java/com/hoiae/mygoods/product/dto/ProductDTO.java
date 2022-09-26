package com.hoiae.mygoods.product.dto;

public class ProductDTO {
    private String result;
    private String image;

    public ProductDTO() {
    }

    public ProductDTO(String result, String image) {
        this.result = result;
        this.image = image;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
