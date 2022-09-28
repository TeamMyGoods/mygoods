package com.hoiae.mygoods.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoiae.mygoods.common.exception.payment.OrderException;
import com.hoiae.mygoods.common.exception.payment.PaymentException;
import com.hoiae.mygoods.common.exception.payment.PriceNotEqualException;
import com.hoiae.mygoods.payment.dao.PaymentMapper;
import com.hoiae.mygoods.payment.dto.ModelDTO;
import com.hoiae.mygoods.payment.dto.OrderDTO;
import com.hoiae.mygoods.payment.dto.PaymentDTO;
import com.hoiae.mygoods.payment.dto.WeekDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentMapper mapper;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

//    private String beforePaymentKey = "100"; // DB에서 가져왔다고 가정
    public PaymentService(PaymentMapper mapper){
        this.mapper = mapper;
    }

//    private final String SECRET_KEY = "test_sk_zXLkKEypNArWmo50nX3lmeaxYG5R";
    private final String SECRET_KEY = "test_ak_mnRQoOaPz8LwjZD1Oljry47BMw6v";
    public void verifyRequest(String paymentKey, String orderId, Long amount) throws PriceNotEqualException {

        String orderPrice = mapper.selectPriceByOrderId(orderId);

        System.out.println(orderPrice);

        if(Long.parseLong(orderPrice) == amount){
            System.out.println("값이 일치합니다.");
        } else{
            System.out.println("값이 일치하지 않습니다.");
            throw new PriceNotEqualException("가격이 일치하지 않아 결제에 실패했습니다.");
        }
    }

//    public String requestFinalPayment(String paymentKey, String orderId, Long amount, Model model) throws IOException, InterruptedException {
//
//        HttpHeaders headers = new HttpHeaders();
//        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
//        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, String> payloadMap = new HashMap<>();
//        payloadMap.put("orderId", orderId);
//        payloadMap.put("amount", String.valueOf(amount));
//
//        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);
//
//        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
//                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);
//
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            JsonNode successNode = responseEntity.getBody();
//            model.addAttribute("orderId", successNode.get("orderId").asText());
//            String secret = successNode.get("secret").asText(); // 가상계좌의 경우 입금 callback 검증을 위해서 secret을 저장하기를 권장함
//            return "success";
//        } else {
//            JsonNode failNode = responseEntity.getBody();
//            model.addAttribute("message", failNode.get("message").asText());
//            model.addAttribute("code", failNode.get("code").asText());
//            return "fail";
//        }
//    }

    public int insertPayment(PaymentDTO payment) throws PaymentException {

        int result = mapper.insertPaymentInfo(payment);

        if(!(result > 0 )){
            throw new PaymentException("결제에 실패하셨습니다.");
        }

        return result;
    }

    public int insertOrder(OrderDTO order) throws OrderException {

        int result = mapper.insertOrder(order);

        if(!(result > 0)){
            throw new OrderException("주문 등록에 실패하셨습니다.");
        }

        return result;
    }

    public String selectUserByUserName(String username) {

        return mapper.selectUserByUserName(username);
    }

    public List<WeekDTO> selectOrderCount() {
        return mapper.selectOrderCount();
    }

    public List<ModelDTO> selectModelCount() {
        return mapper.selectModelCount();
    }
}
