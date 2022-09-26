package com.hoiae.mygoods.product.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoiae.mygoods.product.dto.ProductDTO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.Base64;

@Controller
@RequestMapping("/product")
public class ProductController {

    @GetMapping("")
    public  String product() {
        return "content/product/index";
    }


    @ResponseBody
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image") MultipartFile file) throws IOException, ParseException {

        /*원본 파일명*/
        System.out.println(file.getOriginalFilename());

        /* 파일업로드*/



        //RestTemplate을 이용한 단일 파일 업로드

        /*  create RestTemplate instance*/
        RestTemplate restTemplate = new RestTemplate();

        /*request header*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);


        /*request body*/
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();
        System.out.println(file.getResource());
        body.add("file", file.getResource());

        /*헤더와 본문 개체를 감싸는 HttpEntity 인스턴스를 생성하고 RestTemplate을 사용하여 게시한다.*/
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(body, headers);

        /*restTemplate.postForEntity() 호출은 주어진 URL에 연결하고 파일을 서버로 보내는 작업을 완료*/
        ResponseEntity<String> response = restTemplate.postForEntity("https://7d663fa4-f5b9-47a2-98b1-7cbe746422fd.mock.pstmn.io/product", entity, String.class);

        /*System.out.println(response.getBody());*/

//        ObjectMapper objectMapper = new ObjectMapper();
//
//        ProductDTO productDTO = objectMapper.readValue(response.getBody(),ProductDTO.class);
//
//        System.out.println(productDTO.getResult());
//        System.out.println(productDTO.getImage());

        JSONParser parser = new JSONParser();
        Object obj = parser.parse( response.getBody() );
        JSONObject jsonObj = (JSONObject) obj;

        System.out.println(jsonObj.get("result"));

        String base64example = (String) jsonObj.get("img");

        System.out.println(base64example);


        byte[] decodeData = Base64Utils.decode(base64example.getBytes());
        System.out.println(decodeData.toString());

        System.out.println(Base64Utils.encode(decodeData));


        return response.getBody();

    }
    }
