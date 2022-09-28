package com.hoiae.mygoods.payment.dao;

import com.hoiae.mygoods.payment.dto.ModelDTO;
import com.hoiae.mygoods.payment.dto.OrderDTO;
import com.hoiae.mygoods.payment.dto.PaymentDTO;
import com.hoiae.mygoods.payment.dto.WeekDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {

    int insertPaymentInfo(PaymentDTO payment);

    int insertOrder(OrderDTO order);

    String selectUserByUserName(String username);

    List<WeekDTO> selectOrderCount();

    List<ModelDTO> selectModelCount();

    String selectPriceByOrderId(String orderId);
}
