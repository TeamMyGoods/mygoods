package com.hoiae.mygoods.product.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/product")
public class ProductController {

    @GetMapping("")
    public  String product() {
        return "content/product/index";
    }


    @ResponseBody
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image") MultipartFile file) throws IOException {

        System.out.println(file.getOriginalFilename());

//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//        String serverUrl = "http://192.168.0.58:5000/facev1";
//
//        ResponseEntity<String> response = restTemplate
//                .postForEntity(serverUrl, requestEntity, String.class);
//
//        System.out.println(response);

        //RestTemplate을 이용한 단일 파일 업로드

        /*  create RestTemplate instance*/
        RestTemplate restTemplate = new RestTemplate();

        /*request header*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);


        /*request body*/
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>(); //LinkedMultiValueMap은 LinkedList의 각 키에 대해 여러 값을 저장하는 LinkedHashMap을 래핑
        System.out.println(file.getResource());
        body.add("file", file.getResource()); //리소스 보내기

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(body, headers); //헤더와 본문 개체를 감싸는 HttpEntity 인스턴스를 생성하고 RestTemplate을 사용하여 게시한다.
        ResponseEntity<String> response = restTemplate.postForEntity("https://photo-to-character-rvakk.run.goorm.io/Shinkai", entity, String.class); //restTemplate.postForEntity() 호출은 주어진 URL에 연결하고 파일을 서버로 보내는 작업을 완료

        System.out.println(response.getBody());

//        Object obj = new JSONParser().parse(response.getBody());

        return response.getBody();

    }
    }
