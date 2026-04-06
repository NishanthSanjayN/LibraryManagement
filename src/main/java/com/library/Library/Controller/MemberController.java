package com.library.Library.Controller;

import com.library.Library.model.Member;
import com.library.Library.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {
    
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // UPDATED: Now accepts an optional 'keyword' parameter for searching
    @GetMapping
    public String listMembers(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("members", memberService.findMembers(keyword));
        model.addAttribute("keyword", keyword); 
        return "members/list"; 
    }

    @GetMapping("/add")
    public String showAddMemberForm(Model model) {
        model.addAttribute("member", new Member());
        return "members/add"; 
    }

    // ... (Other methods: addMember, showUpdateForm, updateMember, deleteMember remain unchanged)
    @PostMapping("/add")
    public String addMember(@ModelAttribute Member member) {
        memberService.saveMember(member);
        return "redirect:/members"; 
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Member member = memberService.findMemberById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid member Id:" + id));
        
        model.addAttribute("member", member);
        return "members/edit"; 
    }

    @PostMapping("/update/{id}")
    public String updateMember(@PathVariable("id") long id, @ModelAttribute Member member) {
        member.setId(id);
        memberService.saveMember(member); 
        return "redirect:/members";
    }

    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable("id") long id) {
        memberService.deleteMember(id);
        return "redirect:/members";
    }
}
