package com.example.labomasi.controller;

import com.example.labomasi.entity.Member;
import com.example.labomasi.enums.MemberRole;
import com.example.labomasi.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("members", memberService.search(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("members", memberService.findAll());
        }
        return "member/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        memberService.findById(id).ifPresent(member -> model.addAttribute("member", member));
        return "member/view";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("member", new Member());
        model.addAttribute("roles", MemberRole.values());
        return "member/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Member member,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", MemberRole.values());
            return "member/form";
        }

        if (memberService.existsByEmail(member.getEmail())) {
            result.rejectValue("email", "error.member", "Email already exists");
            model.addAttribute("roles", MemberRole.values());
            return "member/form";
        }

        memberService.save(member);
        redirectAttributes.addFlashAttribute("success", "Member created successfully!");
        return "redirect:/members";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        memberService.findById(id).ifPresent(member -> {
            member.setPassword(""); // Don't show password
            model.addAttribute("member", member);
        });
        model.addAttribute("roles", MemberRole.values());
        return "member/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Member member,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", MemberRole.values());
            return "member/form";
        }

        memberService.update(id, member);
        redirectAttributes.addFlashAttribute("success", "Member updated successfully!");
        return "redirect:/members";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        memberService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Member deleted successfully!");
        return "redirect:/members";
    }
}