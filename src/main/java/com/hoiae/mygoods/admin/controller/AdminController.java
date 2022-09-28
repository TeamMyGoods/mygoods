package com.hoiae.mygoods.admin.controller;

import com.hoiae.mygoods.admin.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }
    @GetMapping("")
    public String goMain(Model model) {

        List<Member> memberList = adminService.findAllMember();
        System.out.println(memberList);

        model.addAttribute("memberList", memberList);

        return "admin/index";
    }

    @GetMapping("/order")
    public String goOrder(){
        return "admin/order";
    }

    @GetMapping("/model")
    public String goModel(){
        return "admin/model";
    }
}
