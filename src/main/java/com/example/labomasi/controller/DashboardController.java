package com.example.labomasi.controller;

import com.example.labomasi.service.MemberService;
import com.example.labomasi.service.ProjectService;
import com.example.labomasi.service.PublicationService;
import com.example.labomasi.service.ResourceService;
import com.example.labomasi.enums.ProjectStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final MemberService memberService;
    private final ProjectService projectService;
    private final PublicationService publicationService;
    private final ResourceService resourceService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProjects", projectService.count());
        stats.put("activeProjects", projectService.countByStatus(ProjectStatus.IN_PROGRESS));
        stats.put("totalMembers", memberService.count());
        stats.put("totalPublications", publicationService.count());
        stats.put("publicationsThisYear", publicationService.countThisYear());
        stats.put("totalResources", resourceService.count());
        stats.put("availableResources", resourceService.countAvailable());

        model.addAttribute("stats", stats);
        model.addAttribute("recentProjects", projectService.findRecent());

        return "dashboard/index";
    }
}
