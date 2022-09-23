package com.hoiae.mygoods.product.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
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
//
//        /* RestTemplate instance*/
//        RestTemplate restTemplate = new RestTemplate();
//
//        /*request header*/
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//
//        /*request body*/
//        MultiValueMap<String, Object> body  = new LinkedMultiValueMap<>();
//        body.add("file", file);
//
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//
//        String serverUrl = "http://192.168.0.58:5000/facev1";
//
//        ResponseEntity<String> response = restTemplate
//                .postForEntity(serverUrl, requestEntity, String.class);
//
//        System.out.println(response);

        //RestTemplate을 이용한 단일 파일 업로드

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders(); //헤더와 본문이 있는 HttpEntity를 만든다.
        headers.setContentType(MediaType.MULTIPART_FORM_DATA); //헤더값을 설정해주면 RestTemplate은 일부 메타 데이터와 함께 파일 데이터를 자동으로 마샬링 한다.

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>(); //LinkedMultiValueMap은 LinkedList의 각 키에 대해 여러 값을 저장하는 LinkedHashMap을 래핑
        body.add("file", file.getResource()); //리소스 보내기

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String,Object>>(body, headers); //헤더와 본문 개체를 감싸는 HttpEntity 인스턴스를 생성하고 RestTemplate을 사용하여 게시한다.
        ResponseEntity<String> response = restTemplate.postForEntity("http://192.168.0.58:5000/facev1", entity, String.class); //restTemplate.postForEntity() 호출은 주어진 URL에 연결하고 파일을 서버로 보내는 작업을 완료

        System.out.println(response.getBody());



        return "redirect:/";
    }





/*    @PostMapping("/fileupload")
    public String fileUpload(@RequestParam MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        File converFile = new File(file.getOriginalFilename());

        System.out.println(file);

        file.transferTo(converFile);
//        file.transferTo(new File(file.getOriginalFilename()));

        String filename_url = file.getOriginalFilename();
        String message = file.getOriginalFilename()+ " 파일이 저장되었습니다.";

        redirectAttributes.addFlashAttribute("message",message);
        redirectAttributes.addFlashAttribute("filename_url",filename_url);

        return "redirect:/product";
    }*/

}
