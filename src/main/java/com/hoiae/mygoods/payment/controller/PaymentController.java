package com.hoiae.mygoods.payment.controller;

import com.hoiae.mygoods.common.payment.PriceNotEqualException;
import com.hoiae.mygoods.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.http.HttpRequest;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @GetMapping("/order")
    public String goOrder(){
        return "content/payment/order";
    }

    @GetMapping("/success")
    public ModelAndView requestPayments(ModelAndView mv, @RequestParam String paymentKey, @RequestParam String orderId, @RequestParam Long amount) throws PriceNotEqualException, IOException, InterruptedException {
        System.out.println("paymentKey : " + paymentKey);
        System.out.println("orderId : " + orderId);
        System.out.println("amount : " + amount);

        paymentService.verifyRequest(paymentKey, orderId, amount);
        String result = paymentService.requestFinalPayment(paymentKey, orderId, amount);

        System.out.println(result);

        mv.setViewName("content/payment/success");
        mv.addObject("response", result);

        return mv;
    }
}
