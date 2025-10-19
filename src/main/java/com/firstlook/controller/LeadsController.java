package com.firstlook.controller;

import com.firstlook.model.LeadScore;
import com.firstlook.repository.LeadScoreRepository;
import com.firstlook.service.LeadScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/leads")
@RequiredArgsConstructor
public class LeadsController {
    
    private final LeadScoringService leadScoringService;
    private final LeadScoreRepository leadScoreRepository;
    
    @GetMapping
    public String leadsPage(Model model) {
        Map<String, Object> leadAnalytics = leadScoringService.getLeadScoringAnalytics();
        List<LeadScore> allLeads = leadScoreRepository.findAll();
        
        // Sort by score descending
        allLeads.sort((a, b) -> b.getScore().compareTo(a.getScore()));
        
        model.addAttribute("leadAnalytics", leadAnalytics);
        model.addAttribute("allLeads", allLeads);
        
        return "admin/leads";
    }
}
