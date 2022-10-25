package org.zerock.b01.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.service.MemberService;

@Log4j2
@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {


    private final MemberService memberService;

    @GetMapping("/login")
    public void loginForm(String error, String logout){
        log.info("login get..............");
        log.info("logout: " + logout);
    }

    @GetMapping("/join")
    public String joinForm(@ModelAttribute MemberJoinDTO memberJoinDTO, Model model){
        model.addAttribute(new MemberJoinDTO());
        return "member/join";
    }
    @PostMapping("/join")
    public String submitJoin(RedirectAttributes redirectAttributes, MemberJoinDTO memberJoinDTO){
       try {
           memberService.join(memberJoinDTO);
       }catch (MemberService.MidExistException e){
           log.info(e.getMessage());
           return "redirect:/member/join";
       }
       redirectAttributes.addFlashAttribute("result","success");
        return "redirect:/member/login";
    }
}
