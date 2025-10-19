package com.firstlook.controller;

import com.firstlook.model.Visitor;
import com.firstlook.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/visitors")
@RequiredArgsConstructor
public class VisitorsViewController {
    
    private final VisitorService visitorService;
    
    @GetMapping
    public String visitorsPage(Model model) {
        Map<String, Object> analytics = visitorService.getAnalytics();
        List<Visitor> recentVisitors = visitorService.getRecentVisitors(50);
        
        model.addAttribute("analytics", analytics);
        model.addAttribute("recentVisitors", recentVisitors);
        
        return "admin/visitors";
    }
}
