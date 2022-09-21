package com.hoiae.mygoods.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.hoiae.mygoods.common.payment.PriceNotEqualException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
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

//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
//                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
//                .header("Content-Type", "application/json")
//                .method("POST", HttpRequest.BodyPublishers.ofString("{\"paymentKey\":\"" + paymentKey + "\",\"amount\":" + amount + ",\"orderId\":\"" + orderId + " \"}"))
//                .build();
//
//        HttpResponse<String> response = (HttpResponse<String>) HttpClient.newHttpClient().send((HttpRequest) request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
//
//        return response.body();

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

//        RestTemplate rest = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//
//        String testSecretApiKey = "test_sk_zXLkKEypNArWmo50nX3lmeaxYG5R:";
//        String encodedAuth = new String(Base64.getEncoder().encode(testSecretApiKey.getBytes(StandardCharsets.UTF_8)));
//        headers.setBasicAuth(encodedAuth);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//        JSONPObject param = new JSONPObject();
//        param.put("orderId", orderId);
//        param.put("amount", amount);
//
//        return rest.postForEntity(tossOriginUrl + )

    }
}
