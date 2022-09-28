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

    /*마이페이지*/
    @GetMapping("/mypage")
    public String goMypage(Model model){
        /*20220927로그인정보 가져오기 테스트*/
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
//        /*주문코드, 상품이름, 상품사이즈, 상품가격,*/
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
    /*캐릭터 이미지 가져오기*/
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

    /*MEMBER_NO가져오기*/
    public int selectMemberNoById(){
        /*20220927로그인정보 가져오기 테스트*/
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;
        String username = ((UserDetails) principal).getUsername();
        System.out.println("username :" + username);
        //username을 통해서 사용자 아이디를 가져올수 있음.
        /*20220927로그인정보 가져오기 테스트*/

        String rootPath = System.getProperty("user.dir");
        System.out.println("루루루트경로: "+rootPath);
        int result = memberService.selectMemberNoById(username);
        System.out.println("memberNo :"+result);
        return result;
    }
}
