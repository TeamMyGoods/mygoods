package com.hoiae.mygoods.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/product")
public class ProductController {

    @GetMapping("")
    public  String product() {
        return "content/product/index";
    }

    @PostMapping("/api")
    @ResponseBody
    public String uploadImage(MultipartHttpServletRequest request){
        System.out.println(request.getSession());
        return "OK";
    }

    @PostMapping("/fileupload")
    public String fileUpload(@RequestParam MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        File converFile = new File(file.getOriginalFilename());

        System.out.println(converFile.getName());

        file.transferTo(converFile);
//        file.transferTo(new File(file.getOriginalFilename()));

        String filename_url = file.getOriginalFilename();
        String message = file.getOriginalFilename()+ " 파일이 저장되었습니다.";

        redirectAttributes.addFlashAttribute("message",message);
        redirectAttributes.addFlashAttribute("filename_url",filename_url);

        return "redirect:/product";
    }

}
