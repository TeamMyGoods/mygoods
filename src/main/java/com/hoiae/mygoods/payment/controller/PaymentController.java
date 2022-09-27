package com.hoiae.mygoods.payment.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoiae.mygoods.common.payment.PriceNotEqualException;
import com.hoiae.mygoods.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PaymentService paymentService;

    private final String SECRET_KEY = "test_sk_zXLkKEypNArWmo50nX3lmeaxYG5R";

    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }
            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }

    @GetMapping("/order")
    public String goOrder(){
        return "content/payment/order";
    }

    @GetMapping("/success")
    public String requestPayments(Model model, @RequestParam String paymentKey, @RequestParam String orderId, @RequestParam Long amount) throws PriceNotEqualException, IOException, InterruptedException {
//        System.out.println("paymentKey : " + paymentKey);
//        System.out.println("orderId : " + orderId);
//        System.out.println("amount : " + amount);
//        String productName = customerName.split("/")[0];
//        String productSize = customerName.split("/")[1];
//        System.out.println(productName);
//        System.out.println(productSize);

        paymentService.verifyRequest(paymentKey, orderId, amount);
//        String result = paymentService.requestFinalPayment(paymentKey, orderId, amount, model);

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

//        model.addAttribute("customerName", customerName);
//        model.addAttribute("orderName", orderName);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode successNode = responseEntity.getBody();
            String productName = successNode.get("orderName").asText().split("/")[0];
            String productSize = successNode.get("orderName").asText().split("/")[1];

            System.out.println(successNode);
            model.addAttribute("orderId", successNode.get("orderId").asText());
            model.addAttribute("productName", productName);
            model.addAttribute("productSize", productSize);

            String secret = successNode.get("secret").asText(); // 가상계좌의 경우 입금 callback 검증을 위해서 secret을 저장하기를 권장함

            // service 로직 필요.
            int result = paymentService.insertPayment(paymentKey, orderId, amount,productName, productSize);
            
            return "content/payment/success";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "content/payment/fail";
        }
    }

    @GetMapping("/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "fail";
    }

    @GetMapping("/testSuccess")
    public String paymentTest(Model model){
        model.addAttribute("orderId", "0-123123123");
        model.addAttribute("productName", "hoiae");
        model.addAttribute("productSize", "ordernamd");
        return "content/payment/success";
    }

    @GetMapping("/testFail")
    public String paymentFail(Model model){
        model.addAttribute("customername", "hoiae");
        return "content/payment/fail";
    }
}
