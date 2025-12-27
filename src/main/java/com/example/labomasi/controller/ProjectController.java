package com.example.labomasi.controller;

import com.example.labomasi.entity.Project;
import com.example.labomasi.enums.ProjectStatus;
import com.example.labomasi.service.MemberService;
import com.example.labomasi.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final MemberService memberService;

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("projects", projectService.search(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("projects", projectService.findAll());
        }
        return "project/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        projectService.findById(id).ifPresent(project -> model.addAttribute("project", project));
        return "project/view";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("members", memberService.findAll());
        return "project/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Project project,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", ProjectStatus.values());
            model.addAttribute("members", memberService.findAll());
            return "project/form";
        }

        projectService.save(project);
        redirectAttributes.addFlashAttribute("success", "Project created successfully!");
        return "redirect:/projects";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        projectService.findById(id).ifPresent(project -> model.addAttribute("project", project));
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("members", memberService.findAll());
        return "project/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Project project,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", ProjectStatus.values());
            model.addAttribute("members", memberService.findAll());
            return "project/form";
        }

        projectService.update(id, project);
        redirectAttributes.addFlashAttribute("success", "Project updated successfully!");
        return "redirect:/projects";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Project deleted successfully!");
        return "redirect:/projects";
    }
}