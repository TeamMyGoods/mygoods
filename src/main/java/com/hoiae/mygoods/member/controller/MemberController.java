package com.hoiae.mygoods.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hoiae.mygoods.common.exception.member.MemberModifyException;
import com.hoiae.mygoods.common.exception.member.MemberRegistException;
import com.hoiae.mygoods.common.exception.member.MemberRemoveException;
import com.hoiae.mygoods.common.util.SessionUtil;
import com.hoiae.mygoods.member.dto.MemberDTO;
import com.hoiae.mygoods.member.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/member")
public class MemberController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    public MemberController(PasswordEncoder passwordEncoder, MemberService memberService) {
        this.passwordEncoder = passwordEncoder;
        this.memberService = memberService;
    }

    @GetMapping("/regist")
    public String goRegister() {
        return "content/member/regist";
    }

    @PostMapping("/regist")
    public String registMember(@ModelAttribute MemberDTO member, HttpServletRequest request,
                               RedirectAttributes rttr) throws MemberRegistException {

        String address = request.getParameter("zipCode") + "$" + request.getParameter("address1") + "$" + request.getParameter("address2");
        member.setPhone(member.getPhone().replace("-", ""));
        member.setAddress(address);
        member.setMemberPwd(passwordEncoder.encode(member.getMemberPwd()));

        memberService.registMember(member);

        rttr.addFlashAttribute("message", "회원가입 성공하였습니다.");

        return "redirect:/";
    }

    @PostMapping("/idDupCheck")
    public ResponseEntity<String> checkDuplication(@RequestBody MemberDTO memberDto) throws JsonProcessingException {

        String result = "사용 가능한 아이디 입니다.";

        if("".equals(memberDto.getMemberId())) {
            result = "아이디를 입력해 주세요";
        } else if(memberService.selectMemberById(memberDto.getMemberId())) {
            log.info("[MemberController] Already Exist");
            result = "중복된 아이디 존재합니다.";
        }

        return ResponseEntity.ok(result); //200전송?
    }


    @GetMapping("/login")
    public String goLogin() {

        return "content/member/login";
    }


    @GetMapping("/loginfail")
    public String goLoginFail() {

        return "errors/login";
    }

    @GetMapping("/update")
    public String goModifyMember() {

        return "content/member/update";
    }

    @PostMapping("/update")
    public String modifyMember(@ModelAttribute MemberDTO member, HttpServletRequest request, HttpServletResponse response,
                               RedirectAttributes rttr) throws MemberModifyException {


        String address = request.getParameter("zipCode") + "$" + request.getParameter("address1") + "$" + request.getParameter("address2");
        member.setPhone(member.getPhone().replace("-", ""));
        member.setAddress(address);
        member.setMemberPwd(passwordEncoder.encode(member.getMemberPwd()));


        memberService.modifyMember(member);

        // 회원정보 수정후 로그아웃 프로세스 진행
        SessionUtil.invalidateSession(request, response);

        rttr.addFlashAttribute("message", "회원 정보 수정에 성공하셨습니다. 다시 로그인해주세요.");

        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteMember(@ModelAttribute MemberDTO member, SessionStatus status
            , RedirectAttributes rttr, HttpServletRequest request, HttpServletResponse response) throws MemberRemoveException {

        String memberId = request.getParameter("id");
        member.setMemberId(memberId);

        memberService.removeMember(member);

        SessionUtil.invalidateSession(request, response);

        rttr.addFlashAttribute("message", "회원탈퇴에 성공하셨습니다. 로그아웃됩니다.");

        return "redirect:/";
    }
}
