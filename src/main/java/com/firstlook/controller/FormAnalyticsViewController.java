package com.firstlook.controller;

import com.firstlook.service.FormAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/admin/analytics")
@RequiredArgsConstructor
public class FormAnalyticsViewController {
    
    private final FormAnalyticsService formAnalyticsService;
    
    @GetMapping
    public String analyticsPage(Model model) {
        Map<String, Object> formAnalytics = formAnalyticsService.getFormAnalytics();
        
        model.addAttribute("formAnalytics", formAnalytics);
        
        return "admin/form-analytics";
    }
}
