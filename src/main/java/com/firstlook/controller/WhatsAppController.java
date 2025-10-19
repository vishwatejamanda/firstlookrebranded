package com.firstlook.controller;

import com.firstlook.service.WhatsAppService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp")
@RequiredArgsConstructor
public class WhatsAppController {
    
    private final WhatsAppService whatsAppService;
    
    @PostMapping("/track")
    public ResponseEntity<Map<String, Object>> trackWhatsAppClick(
            @RequestParam(required = false) String sourcePage,
            HttpServletRequest request) {
        
        whatsAppService.trackWhatsAppClick(request, sourcePage != null ? sourcePage : "unknown");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/url")
    public ResponseEntity<Map<String, String>> getWhatsAppUrl(
            @RequestParam(required = false) String message) {
        
        String url = whatsAppService.generateWhatsAppUrl(message);
        
        Map<String, String> response = new HashMap<>();
        response.put("url", url);
        return ResponseEntity.ok(response);
    }
}
