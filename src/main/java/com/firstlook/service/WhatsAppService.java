package com.firstlook.service;

import com.firstlook.model.WhatsAppClick;
import com.firstlook.repository.WhatsAppClickRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhatsAppService {
    
    private final WhatsAppClickRepository whatsAppClickRepository;
    
    @Value("${whatsapp.phone.number}")
    private String phoneNumber;
    
    @Value("${whatsapp.default.message}")
    private String defaultMessage;
    
    @Transactional
    public void trackWhatsAppClick(HttpServletRequest request, String sourcePage) {
        try {
            WhatsAppClick click = new WhatsAppClick();
            click.setIpAddress(getClientIp(request));
            click.setSourcePage(sourcePage);
            click.setUserAgent(request.getHeader("User-Agent"));
            
            whatsAppClickRepository.save(click);
            log.info("WhatsApp click tracked: IP={}, Source={}", click.getIpAddress(), sourcePage);
        } catch (Exception e) {
            log.error("Error tracking WhatsApp click", e);
        }
    }
    
    public String generateWhatsAppUrl(String customMessage) {
        String message = customMessage != null && !customMessage.isEmpty() 
            ? customMessage 
            : defaultMessage;
        
        return String.format("https://wa.me/%s?text=%s", 
            phoneNumber, 
            java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8));
    }
    
    public Long getTotalClicks() {
        return whatsAppClickRepository.countTotalClicks();
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
