package com.park.mall.web.admin;

import com.park.mall.domain.member.Member;
import com.park.mall.repository.member.MemberJpaRepository;
import com.park.mall.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class MainController {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @GetMapping("/main")
    public String mainPage(Model model) {
        Pageable pageable = PageRequest.of(0, 10);
        List<Member> memberList = memberJpaRepository.findTop10WithQuery(pageable);
        model.addAttribute("memberList", memberList);
        return "admin/main";
    }
}
