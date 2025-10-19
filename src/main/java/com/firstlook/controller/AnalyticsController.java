package com.firstlook.controller;

import com.firstlook.model.ContactMessage;
import com.firstlook.model.LeadScore;
import com.firstlook.model.Visitor;
import com.firstlook.repository.LeadScoreRepository;
import com.firstlook.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AnalyticsController {
    
    private final VisitorService visitorService;
    private final ContactService contactService;
    private final WhatsAppService whatsAppService;
    private final FormAnalyticsService formAnalyticsService;
    private final LeadScoringService leadScoringService;
    private final LeadScoreRepository leadScoreRepository;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // Existing analytics
            Map<String, Object> analytics = visitorService.getAnalytics();
            List<Visitor> recentVisitors = visitorService.getRecentVisitors(10);
            List<ContactMessage> recentMessages = contactService.getAllMessages();
            Long whatsappClicks = whatsAppService.getTotalClicks();
            
            // New analytics
            Map<String, Object> formAnalytics = formAnalyticsService.getFormAnalytics();
            Map<String, Object> leadAnalytics = leadScoringService.getLeadScoringAnalytics();
            List<LeadScore> hotLeads = leadScoringService.getHotLeads();
            
            // Enrich messages with lead scores (safely)
            recentMessages.forEach(msg -> {
                try {
                    leadScoreRepository.findByContactMessageId(msg.getId()).ifPresent(score -> {
                        msg.setStatus(msg.getStatus() + " [" + score.getQuality() + " - " + score.getScore() + "]");
                    });
                } catch (Exception e) {
                    // Ignore if lead score not found
                }
            });
            
            model.addAttribute("analytics", analytics);
            model.addAttribute("recentVisitors", recentVisitors);
            model.addAttribute("recentMessages", recentMessages);
            model.addAttribute("whatsappClicks", whatsappClicks);
            model.addAttribute("formAnalytics", formAnalytics);
            model.addAttribute("leadAnalytics", leadAnalytics);
            model.addAttribute("hotLeads", hotLeads);
            
            return "admin/dashboard-new";
        } catch (Exception e) {
            e.printStackTrace();
            // Initialize empty values to prevent errors
            model.addAttribute("analytics", new java.util.HashMap<>());
            model.addAttribute("recentVisitors", new java.util.ArrayList<>());
            model.addAttribute("recentMessages", new java.util.ArrayList<>());
            model.addAttribute("whatsappClicks", 0L);
            model.addAttribute("formAnalytics", new java.util.HashMap<>());
            model.addAttribute("leadAnalytics", new java.util.HashMap<>());
            model.addAttribute("hotLeads", new java.util.ArrayList<>());
            model.addAttribute("error", e.getMessage());
            return "admin/dashboard-new";  // Use new dashboard with empty data
        }
    }
    
    @GetMapping("/dashboard/old")
    public String dashboardOld(Model model) {
        // Keep old dashboard for reference
        Map<String, Object> analytics = visitorService.getAnalytics();
        List<Visitor> recentVisitors = visitorService.getRecentVisitors(10);
        List<ContactMessage> recentMessages = contactService.getAllMessages();
        Long whatsappClicks = whatsAppService.getTotalClicks();
        
        Map<String, Object> formAnalytics = formAnalyticsService.getFormAnalytics();
        Map<String, Object> leadAnalytics = leadScoringService.getLeadScoringAnalytics();
        List<LeadScore> hotLeads = leadScoringService.getHotLeads();
        
        model.addAttribute("analytics", analytics);
        model.addAttribute("recentVisitors", recentVisitors);
        model.addAttribute("recentMessages", recentMessages);
        model.addAttribute("whatsappClicks", whatsappClicks);
        model.addAttribute("formAnalytics", formAnalytics);
        model.addAttribute("leadAnalytics", leadAnalytics);
        model.addAttribute("hotLeads", hotLeads);
        
        return "admin/dashboard";
    }
    
    @GetMapping("/lead/{id}")
    public String viewLead(@PathVariable Long id, Model model) {
        // Future: Detailed lead view with full journey
        return "admin/lead-detail";
    }
}
