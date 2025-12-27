package com.example.labomasi.controller;

import com.example.labomasi.enums.ProjectStatus;
import com.example.labomasi.repository.MemberRepository;
import com.example.labomasi.repository.ProjectRepository;
import com.example.labomasi.repository.PublicationRepository;
import com.example.labomasi.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    // Make these optional - comment out if you don't have them yet
    // private final PublicationRepository publicationRepository;
    // private final ResourceRepository resourceRepository;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProjects", projectRepository.count());
        stats.put("activeProjects", projectRepository.countByStatus(ProjectStatus.IN_PROGRESS));
        stats.put("totalMembers", memberRepository.count());

        // Set to 0 if repositories don't exist yet
        stats.put("totalPublications", 0L);
        stats.put("publicationsThisYear", 0L);
        stats.put("totalResources", 0L);
        stats.put("availableResources", 0L);

        model.addAttribute("stats", stats);
        model.addAttribute("recentProjects", projectRepository.findTop5ByOrderByCreatedAtDesc());

        return "dashboard/index";
    }
}