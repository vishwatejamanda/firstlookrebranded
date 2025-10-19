package com.firstlook.service;

import com.firstlook.model.ContactMessage;
import com.firstlook.model.LeadScore;
import com.firstlook.repository.ContactMessageRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {
    
    private final ContactMessageRepository contactMessageRepository;
    private final JavaMailSender mailSender;
    private final LeadScoringService leadScoringService;
    private final CustomerJourneyService customerJourneyService;
    
    @Transactional
    public ContactMessage saveContactMessage(ContactMessage message, HttpServletRequest request) {
        message.setIpAddress(getClientIp(request));
        ContactMessage saved = contactMessageRepository.save(message);
        log.info("Contact message saved: ID={}, Email={}", saved.getId(), saved.getEmail());
        
        // Calculate lead score
        try {
            LeadScore leadScore = leadScoringService.calculateLeadScore(saved);
            log.info("Lead score: {} - Quality: {}", leadScore.getScore(), leadScore.getQuality());
        } catch (Exception e) {
            log.error("Failed to calculate lead score", e);
        }
        
        // Track customer journey
        try {
            customerJourneyService.trackEvent("FORM_SUBMIT", 
                "Contact form submitted: " + saved.getSubject(), request);
        } catch (Exception e) {
            log.error("Failed to track customer journey", e);
        }
        
        // Send email notification
        try {
            sendEmailNotification(saved);
        } catch (Exception e) {
            log.error("Failed to send email notification", e);
        }
        
        return saved;
    }
    
    public List<ContactMessage> getAllMessages() {
        return contactMessageRepository.findByOrderBySubmittedAtDesc();
    }
    
    public List<ContactMessage> getMessagesByStatus(String status) {
        return contactMessageRepository.findByStatus(status);
    }
    
    private void sendEmailNotification(ContactMessage message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("your-email@gmail.com"); // Configure this
        mailMessage.setSubject("New Contact Form Submission: " + message.getSubject());
        mailMessage.setText(
            "New contact form submission:\n\n" +
            "Name: " + message.getName() + "\n" +
            "Email: " + message.getEmail() + "\n" +
            "Subject: " + message.getSubject() + "\n\n" +
            "Message:\n" + message.getMessage()
        );
        
        mailSender.send(mailMessage);
        log.info("Email notification sent for contact message ID: {}", message.getId());
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip != null ? ip.split(",")[0].trim() : "unknown";
    }
}
