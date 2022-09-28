package com.hoiae.mygoods.member.service;

import com.hoiae.mygoods.common.exception.member.MemberModifyException;
import com.hoiae.mygoods.common.exception.member.MemberRegistException;
import com.hoiae.mygoods.common.exception.member.MemberRemoveException;
import com.hoiae.mygoods.member.dao.MemberMapper;
import com.hoiae.mygoods.member.dto.FindOrderDTO;
import com.hoiae.mygoods.member.dto.MemberDTO;
import com.hoiae.mygoods.member.dto.OrderHistoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

        private final Logger log = LoggerFactory.getLogger(this.getClass());

        private final PasswordEncoder passwordEncoder;

        private final MemberMapper mapper;

        public MemberService(PasswordEncoder passwordEncoder, MemberMapper mapper) {
            this.passwordEncoder = passwordEncoder;
            this.mapper = mapper;
        }

        public boolean selectMemberById(String userId) {
            String result = mapper.selectMemberById(userId);

            return result != null? true : false;
        }

        @Transactional
        public void registMember(MemberDTO member) throws MemberRegistException {

            log.info("[MemberService] Insert Member : " + member);
            int result = mapper.insertMember(member);

            log.info("[MemberService] Insert result : " + ((result > 0) ? "회원가입 성공" : "회원가입 실패"));

            if(!(result > 0 )){
                throw new MemberRegistException("회원 가입에 실패하였습니다.");
            }
        }

        public void modifyMember(MemberDTO member) throws MemberModifyException {
            int result = mapper.updateMember(member);

            if(!(result > 0)) {
                throw new MemberModifyException("회원 정보수정에 실패하셨습니다.");
            }
        }

        public void removeMember(MemberDTO member) throws MemberRemoveException {
            int result = mapper.deleteMember(member);

            if(!(result > 0)) {
                throw new MemberRemoveException("회원탈퇴에 실패하셨습니다.");
            }
        }

        public List<OrderHistoryDTO> findOrderList(int memberNo) {

            List<OrderHistoryDTO> orderList = mapper.findOrderList(memberNo);

            return orderList;
//        return result != null? true : false;
    }

    public int selectMemberNoById(String username) {

        return mapper.selectMemberNoById(username);
    }
}
