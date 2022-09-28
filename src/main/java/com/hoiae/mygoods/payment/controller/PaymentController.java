package com.hoiae.mygoods.payment.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoiae.mygoods.common.exception.payment.OrderException;
import com.hoiae.mygoods.common.exception.payment.PaymentException;
import com.hoiae.mygoods.common.exception.payment.PriceNotEqualException;
import com.hoiae.mygoods.payment.dto.ModelDTO;
import com.hoiae.mygoods.payment.dto.OrderDTO;
import com.hoiae.mygoods.payment.dto.PaymentDTO;
import com.hoiae.mygoods.payment.dto.WeekDTO;
import com.hoiae.mygoods.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @PostMapping("/order")
    public ResponseEntity<String> insertOrder(@RequestBody Map<String, String> orderInfo) throws OrderException, ParseException {

        System.out.println("test");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();

        int memberNo = Integer.parseInt(paymentService.selectUserByUserName(username));

        String orderCode = orderInfo.get("orderCode");
        String orderSize = orderInfo.get("orderSize");
        int amount = Integer.parseInt(orderInfo.get("amount"));
        System.out.println("amount = " + amount);

        Date date = new Date(); // 서버 시간으로 사용
        int productCode = Integer.parseInt(orderInfo.get("productCode"));

        OrderDTO order = new OrderDTO(orderCode, orderSize, amount, date,  memberNo, productCode);
        System.out.println(order);

        int result = paymentService.insertOrder(order);
        System.out.println(result);

        String message = "주문내역 입력 성공";

        return ResponseEntity.ok(message);
    }

    @ResponseBody
    @PostMapping("/orderCount")
    public List<WeekDTO> countOrder(){

        List<WeekDTO> result = paymentService.selectOrderCount();

        return result;
    }

    @ResponseBody
    @PostMapping("/model")
    public List<ModelDTO> countModel(){

        List<ModelDTO> result = paymentService.selectModelCount();

        return result;
    }

    @GetMapping("/success")
    public String requestPayments(Model model, @RequestParam String paymentKey, @RequestParam String orderId, @RequestParam Long amount) throws PriceNotEqualException, IOException, InterruptedException, PaymentException {
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

            PaymentDTO payment = new PaymentDTO(paymentKey, "success", orderId, "N");
            // service 로직 필요.
            int result = paymentService.insertPayment(payment);
            
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
