package com.hoiae.mygoods.payment.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.hoiae.mygoods.common.payment.PriceNotEqualException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private String beforePaymentKey = "100"; // DB에서 가져왔다고 가정

    private final String SECRET_KEY = "test_sk_zXLkKEypNArWmo50nX3lmeaxYG5R";

    public void verifyRequest(String paymentKey, String orderId, Long amount) throws PriceNotEqualException {

        if(Long.parseLong(beforePaymentKey) == amount){
            System.out.println("값이 일치합니다.");
        } else{
            System.out.println("값이 일치하지 않습니다.");
            throw new PriceNotEqualException("가격이 일치하지 않아 결제에 실패했습니다.");
        }
    }

    public String requestFinalPayment(String paymentKey, String orderId, Long amount) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()))
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"paymentKey\":\"" + paymentKey + "\",\"amount\":" + amount + ",\"orderId\":\"" + orderId + " \"}"))
                .build();

        HttpResponse<String> response = (HttpResponse<String>) HttpClient.newHttpClient().send((HttpRequest) request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        return response.body();


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
