package com.example.labomasi.controller;

import com.example.labomasi.entity.Resource;
import com.example.labomasi.enums.ResourceCategory;
import com.example.labomasi.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("resources", resourceService.search(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("resources", resourceService.findAll());
        }
        return "resource/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        resourceService.findById(id).ifPresent(resource -> model.addAttribute("resource", resource));
        return "resource/view";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("resource", new Resource());
        model.addAttribute("categories", ResourceCategory.values());
        return "resource/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Resource resource,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", ResourceCategory.values());
            return "resource/form";
        }

        resourceService.save(resource);
        redirectAttributes.addFlashAttribute("success", "Resource created successfully!");
        return "redirect:/resources";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        resourceService.findById(id).ifPresent(resource -> model.addAttribute("resource", resource));
        model.addAttribute("categories", ResourceCategory.values());
        return "resource/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Resource resource,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", ResourceCategory.values());
            return "resource/form";
        }

        resourceService.update(id, resource);
        redirectAttributes.addFlashAttribute("success", "Resource updated successfully!");
        return "redirect:/resources";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        resourceService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Resource deleted successfully!");
        return "redirect:/resources";
    }
}