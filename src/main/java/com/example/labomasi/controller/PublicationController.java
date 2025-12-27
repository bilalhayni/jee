package com.example.labomasi.controller;

import com.example.labomasi.entity.Publication;
import com.example.labomasi.service.MemberService;
import com.example.labomasi.service.ProjectService;
import com.example.labomasi.service.PublicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/publications")
@RequiredArgsConstructor
public class PublicationController {

    private final PublicationService publicationService;
    private final MemberService memberService;
    private final ProjectService projectService;

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("publications", publicationService.search(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("publications", publicationService.findAll());
        }
        return "publication/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        publicationService.findById(id).ifPresent(pub -> model.addAttribute("publication", pub));
        return "publication/view";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'TEACHER')")
    public String showCreateForm(Model model) {
        model.addAttribute("publication", new Publication());
        model.addAttribute("members", memberService.findAll());
        model.addAttribute("projects", projectService.findAll());
        return "publication/form";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'TEACHER')")
    public String create(@Valid @ModelAttribute Publication publication,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("members", memberService.findAll());
            model.addAttribute("projects", projectService.findAll());
            return "publication/form";
        }

        publicationService.save(publication);
        redirectAttributes.addFlashAttribute("success", "Publication created successfully!");
        return "redirect:/publications";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'TEACHER')")
    public String showEditForm(@PathVariable Long id, Model model) {
        publicationService.findById(id).ifPresent(pub -> model.addAttribute("publication", pub));
        model.addAttribute("members", memberService.findAll());
        model.addAttribute("projects", projectService.findAll());
        return "publication/form";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR', 'TEACHER')")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Publication publication,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("members", memberService.findAll());
            model.addAttribute("projects", projectService.findAll());
            return "publication/form";
        }

        publicationService.update(id, publication);
        redirectAttributes.addFlashAttribute("success", "Publication updated successfully!");
        return "redirect:/publications";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        publicationService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Publication deleted successfully!");
        return "redirect:/publications";
    }
}
