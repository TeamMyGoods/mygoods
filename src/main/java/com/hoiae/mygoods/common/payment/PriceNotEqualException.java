package com.hoiae.mygoods.common.payment;

public class PriceNotEqualException extends Throwable {

    public PriceNotEqualException(){}

    public PriceNotEqualException(String msg){
        super(msg);
    }

}
