package com.hoiae.mygoods.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hoiae.mygoods.common.exception.member.MemberModifyException;
import com.hoiae.mygoods.common.exception.member.MemberRegistException;
import com.hoiae.mygoods.common.exception.member.MemberRemoveException;
import com.hoiae.mygoods.common.util.SessionUtil;
import com.hoiae.mygoods.member.dto.MyCharacterDTO;
import com.hoiae.mygoods.member.dto.MemberDTO;
import com.hoiae.mygoods.member.dto.OrderHistoryDTO;
import com.hoiae.mygoods.member.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


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

//        String address = request.getParameter("zipCode") + "$" + request.getParameter("address1") + "$" + request.getParameter("address2");
        member.setPhone(member.getPhone().replace("-", ""));
//        member.setAddress(address);
        member.setMemberPwd(passwordEncoder.encode(member.getMemberPwd()));

        memberService.registMember(member);

        rttr.addFlashAttribute("message", "???????????? ?????????????????????.");

        return "redirect:/";
    }

    @PostMapping("/idDupCheck")
    public ResponseEntity<String> checkDuplication(@RequestBody MemberDTO memberDto) throws JsonProcessingException {

        String result = "?????? ????????? ????????? ?????????.";

        if("".equals(memberDto.getMemberId())) {
            result = "???????????? ????????? ?????????";
        } else if(memberService.selectMemberById(memberDto.getMemberId())) {
            log.info("[MemberController] Already Exist");
            result = "????????? ????????? ???????????????.";
        }

        return ResponseEntity.ok(result); //200???????
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
        member.setMemberPwd(passwordEncoder.encode(member.getMemberPwd()));


        memberService.modifyMember(member);

        // ???????????? ????????? ???????????? ???????????? ??????
        SessionUtil.invalidateSession(request, response);

        rttr.addFlashAttribute("message", "?????? ?????? ????????? ?????????????????????. ?????? ?????????????????????.");

        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteMember(@ModelAttribute MemberDTO member, SessionStatus status
            , RedirectAttributes rttr, HttpServletRequest request, HttpServletResponse response) throws MemberRemoveException {

        String memberId = request.getParameter("id");
        member.setMemberId(memberId);

        memberService.removeMember(member);

        SessionUtil.invalidateSession(request, response);

        rttr.addFlashAttribute("message", "??????????????? ?????????????????????. ?????????????????????.");

        return "redirect:/";
    }

    /*???????????????*/
    @GetMapping("/mypage")
    public String goMypage(Model model){
        /*20220927??????????????? ???????????? ?????????*/
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        String username = ((UserDetails) principal).getUsername();
        String password = ((UserDetails) principal).getPassword();

        System.out.println("username :" + username);
        System.out.println("password :" + password);

        /**/

        int memberNo = selectMemberNoById();
        System.out.println("findOrder:"+memberNo);

        List<OrderHistoryDTO> orderList = memberService.findOrderList(memberNo);
        orderList.forEach(System.out::println);
        model.addAttribute("orderList", orderList );

        /**/

//        int memberNo = selectMemberNoById();
//        System.out.println("findOrder:"+memberNo);

        List<MyCharacterDTO> characterList = memberService.findCharacterList(memberNo);
//        System.out.println("=========================================");
//        characterList.forEach(System.out::println);
//        System.out.println("=========================================");
        model.addAttribute("characterList", characterList );

        return "content/member/mypage";
    }

//    @GetMapping("/orderHistory")
//    public ModelAndView goOrderHistory(ModelAndView mv) {
//
//        /*????????????, ????????????, ???????????????, ????????????,*/
//        mv.addObject("testparam", "testparam" );
//        mv.setViewName("/content/member/orderHistory");
//        return mv;
//    }
//    @GetMapping("/orderHistory")
//    public ModelAndView findOrder(ModelAndView mv){
//        int memberNo = selectMemberNoById();
//        System.out.println("findOrder:"+memberNo);
//
//        List<FindOrderDTO> orderList = memberService.findOrderList(memberNo);
//        orderList.forEach(System.out::println);
//        mv.addObject("orderList", orderList );
//        mv.setViewName("content/member/orderHistory");
//        return mv;
//    }

    @GetMapping("/orderHistory")
    public ModelAndView findOrder(ModelAndView mv){

        int memberNo = selectMemberNoById();
        System.out.println("findOrder:"+memberNo);

        List<OrderHistoryDTO> orderList = memberService.findOrderList(memberNo);
        orderList.forEach(System.out::println);
        mv.addObject("orderList", orderList );
        mv.setViewName("content/member/orderHistory");
        return mv;
    }
    /*????????? ????????? ????????????*/
    @GetMapping("/character")
    public ModelAndView findCharacterList(ModelAndView mv){
        System.out.println("asdfasdafdsfda");
        int memberNo = selectMemberNoById();
        System.out.println("findOrder:"+memberNo);

        List<MyCharacterDTO> characterList = memberService.findCharacterList(memberNo);
        System.out.println("=========================================");
        characterList.forEach(System.out::println);
        System.out.println("=========================================");
        mv.addObject("characterList", characterList );
        mv.setViewName("content/member/mycharacter");
        return mv;
    }

    /*MEMBER_NO????????????*/
    public int selectMemberNoById(){
        /*20220927??????????????? ???????????? ?????????*/
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();
        System.out.println("username :" + username);
        //username??? ????????? ????????? ???????????? ???????????? ??????.
        /*20220927??????????????? ???????????? ?????????*/

        String rootPath = System.getProperty("user.dir");
        System.out.println("??????????????????: "+rootPath);
        int result = memberService.selectMemberNoById(username);
        System.out.println("memberNo :"+result);
        return result;
    }
}
