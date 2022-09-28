package com.hoiae.mygoods.admin.service;

import com.hoiae.mygoods.admin.controller.AdminController;
import com.hoiae.mygoods.member.dao.MemberMapper;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.List;

@Service
public class AdminService {

    private final MemberMapper mapper;

    public AdminService(MemberMapper mapper){
        this.mapper = mapper;
    }
    public List<Member> findAllMember() {
        return mapper.findAllMember();
    }
}
