package com.example.labomasi.controller;

import com.example.labomasi.entity.Member;
import com.example.labomasi.entity.Publication;
import com.example.labomasi.entity.Project;
import com.example.labomasi.enums.MemberRole;
import com.example.labomasi.security.CustomUserDetails;
import com.example.labomasi.service.MemberService;
import com.example.labomasi.service.ProjectService;
import com.example.labomasi.service.PublicationService;
import com.example.labomasi.service.ResourceService;
import com.example.labomasi.enums.ProjectStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        // Get the current user's role
        MemberRole userRole = null;
        Member currentMember = null;

        if (auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            currentMember = userDetails.getMember();
            userRole = currentMember.getRole();
        }

        if (userRole == null) {
            return "redirect:/login";
        }

        // Add current user info to model
        model.addAttribute("currentUser", currentMember);
        model.addAttribute("userRole", userRole);

        // Route to role-specific dashboard with role-specific data
        switch (userRole) {
            case ADMIN:
                return loadAdminDashboard(model);
            case DIRECTOR:
                return loadDirectorDashboard(model, currentMember);
            case TEACHER:
                return loadTeacherDashboard(model, currentMember);
            case DOCTORANT:
                return loadStudentDashboard(model, currentMember);
            default:
                return loadDefaultDashboard(model);
        }
    }

    private String loadAdminDashboard(Model model) {
        // Admin sees all data - full statistics
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
        model.addAttribute("allMembers", memberService.findAll());

        return "dashboard/admin";
    }

    private String loadDirectorDashboard(Model model, Member director) {
        // Director sees lab-wide statistics but focused on research output
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

        // Director-specific: recent publications for oversight
        model.addAttribute("recentPublications", publicationService.findRecent(5));

        return "dashboard/director";
    }

    private String loadTeacherDashboard(Model model, Member teacher) {
        // Teacher sees their own projects and publications
        Map<String, Object> stats = new HashMap<>();

        // Get teacher's own publications
        List<Publication> myPublications = publicationService.findByAuthor(teacher.getId());

        // Get projects where the teacher is involved (as lead or participant)
        List<Project> myProjects = projectService.findByMember(teacher.getId());

        stats.put("myPublications", myPublications.size());
        stats.put("myProjects", myProjects.size());
        stats.put("totalPublications", publicationService.count());
        stats.put("publicationsThisYear", publicationService.countThisYear());

        model.addAttribute("stats", stats);
        model.addAttribute("myProjects", myProjects);
        model.addAttribute("myPublications", myPublications);
        model.addAttribute("recentProjects", projectService.findRecent());

        return "dashboard/teacher";
    }

    private String loadStudentDashboard(Model model, Member student) {
        // Student (Doctorant) sees their own work and public resources
        Map<String, Object> stats = new HashMap<>();

        // Get student's own publications
        List<Publication> myPublications = publicationService.findByAuthor(student.getId());

        // Get projects where the student is involved
        List<Project> myProjects = projectService.findByMember(student.getId());

        stats.put("myPublications", myPublications.size());
        stats.put("myProjects", myProjects.size());
        stats.put("availableResources", resourceService.countAvailable());

        model.addAttribute("stats", stats);
        model.addAttribute("myProjects", myProjects);
        model.addAttribute("myPublications", myPublications);

        return "dashboard/student";
    }

    private String loadDefaultDashboard(Model model) {
        // Fallback dashboard with basic stats
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProjects", projectService.count());
        stats.put("activeProjects", projectService.countByStatus(ProjectStatus.IN_PROGRESS));
        stats.put("totalPublications", publicationService.count());

        model.addAttribute("stats", stats);
        model.addAttribute("recentProjects", projectService.findRecent());

        return "dashboard/index";
    }
}
