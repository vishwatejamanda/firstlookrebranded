package com.firstlook.controller;

import com.firstlook.service.FormAnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/form-analytics")
@RequiredArgsConstructor
public class FormAnalyticsController {
    
    private final FormAnalyticsService formAnalyticsService;
    
    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> trackFormStart(HttpServletRequest request) {
        String sessionId = formAnalyticsService.trackFormStart(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("sessionId", sessionId);
        response.put("success", "true");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> trackFormSubmit(
            @RequestParam String sessionId) {
        
        formAnalyticsService.trackFormSubmit(sessionId);
        
        Map<String, String> response = new HashMap<>();
        response.put("success", "true");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/abandon")
    public ResponseEntity<Map<String, String>> trackFormAbandonment(
            @RequestParam String sessionId) {
        
        formAnalyticsService.trackFormAbandonment(sessionId);
        
        Map<String, String> response = new HashMap<>();
        response.put("success", "true");
        
        return ResponseEntity.ok(response);
    }
}
