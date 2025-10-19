package com.firstlook.controller;

import com.firstlook.model.ContactMessage;
import com.firstlook.repository.ContactMessageRepository;
import com.firstlook.service.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {
    
    private final ContactService contactService;
    private final ContactMessageRepository contactMessageRepository;
    
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitContactForm(
            @Valid @RequestBody ContactMessage message,
            HttpServletRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            ContactMessage saved = contactService.saveContactMessage(message, request);
            response.put("success", true);
            response.put("message", "Thank you for contacting us! We'll get back to you soon.");
            response.put("id", saved.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred. Please try again later.");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/catalog/track-open")
    public ResponseEntity<Map<String, Object>> trackCatalogOpen(@RequestParam String contactId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            ContactMessage contact = contactMessageRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
            
            contact.setCatalogOpened(true);
            contact.setCatalogOpenedAt(LocalDateTime.now());
            contact.setCatalogMessageSent(true);
            contactMessageRepository.save(contact);
            
            response.put("success", true);
            response.put("message", "Catalog open tracked");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/catalog/track-failed")
    public ResponseEntity<Map<String, Object>> trackCatalogFailed(
            @RequestParam String contactId,
            @RequestParam String reason) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            ContactMessage contact = contactMessageRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
            
            contact.setCatalogSendFailed(true);
            contact.setCatalogFailureReason(reason);
            contactMessageRepository.save(contact);
            
            response.put("success", true);
            response.put("message", "Catalog failure tracked");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
