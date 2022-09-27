package com.hoiae.mygoods.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoiae.mygoods.common.payment.PriceNotEqualException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String beforePaymentKey = "100"; // DB에서 가져왔다고 가정

//    private final String SECRET_KEY = "test_sk_zXLkKEypNArWmo50nX3lmeaxYG5R";

    private final String SECRET_KEY = "test_ak_mnRQoOaPz8LwjZD1Oljry47BMw6v";
    public void verifyRequest(String paymentKey, String orderId, Long amount) throws PriceNotEqualException {

        if(Long.parseLong(beforePaymentKey) == amount){
            System.out.println("값이 일치합니다.");
        } else{
            System.out.println("값이 일치하지 않습니다.");
            throw new PriceNotEqualException("가격이 일치하지 않아 결제에 실패했습니다.");
        }
    }

    public String requestFinalPayment(String paymentKey, String orderId, Long amount, Model model) throws IOException, InterruptedException {

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

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("orderId", successNode.get("orderId").asText());
            String secret = successNode.get("secret").asText(); // 가상계좌의 경우 입금 callback 검증을 위해서 secret을 저장하기를 권장함
            return "success";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "fail";
        }
    }

    public int insertPayment(String paymentKey, String orderId, Long amount, String productName, String productSize) {

        return 0;
    }
}
