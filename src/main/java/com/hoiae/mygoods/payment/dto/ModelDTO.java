package com.hoiae.mygoods.payment.dto;

public class ModelDTO {

    private String modelName;
    private int count;

    public ModelDTO() {
    }

    public ModelDTO(String modelName, int count) {
        this.modelName = modelName;
        this.count = count;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ModelDTO{" +
                "modelName='" + modelName + '\'' +
                ", count=" + count +
                '}';
    }
}
