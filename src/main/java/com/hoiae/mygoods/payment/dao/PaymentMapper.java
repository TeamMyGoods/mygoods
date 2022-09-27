package com.hoiae.mygoods.payment.dao;

import com.hoiae.mygoods.payment.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {

    int insertPaymentInfo(String paymentCode, String orderId);

    int insertOrder(OrderDTO order);
}
