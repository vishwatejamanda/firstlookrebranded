package com.firstlook.service;

import com.firstlook.model.FormAnalytics;
import com.firstlook.repository.FormAnalyticsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormAnalyticsService {
    
    private final FormAnalyticsRepository formAnalyticsRepository;
    
    @Transactional
    public String trackFormStart(HttpServletRequest request) {
        String sessionId = UUID.randomUUID().toString();
        
        FormAnalytics analytics = new FormAnalytics();
        analytics.setSessionId(sessionId);
        analytics.setIpAddress(getClientIp(request));
        analytics.setFormStartedAt(LocalDateTime.now());
        analytics.setDeviceType(detectDeviceType(request.getHeader("User-Agent")));
        analytics.setReferrerUrl(request.getHeader("Referer"));
        analytics.setUserAgent(request.getHeader("User-Agent"));
        analytics.setFieldInteractions(0);
        
        formAnalyticsRepository.save(analytics);
        log.info("Form started tracking: sessionId={}", sessionId);
        
        return sessionId;
    }
    
    @Transactional
    public void trackFormSubmit(String sessionId) {
        formAnalyticsRepository.findBySessionId(sessionId).ifPresent(analytics -> {
            analytics.setFormSubmittedAt(LocalDateTime.now());
            analytics.setFormAbandoned(false);
            
            if (analytics.getFormStartedAt() != null) {
                long seconds = ChronoUnit.SECONDS.between(
                    analytics.getFormStartedAt(), 
                    analytics.getFormSubmittedAt()
                );
                analytics.setTimeSpentSeconds((int) seconds);
            }
            
            formAnalyticsRepository.save(analytics);
            log.info("Form submitted: sessionId={}, timeSpent={}s", sessionId, analytics.getTimeSpentSeconds());
        });
    }
    
    @Transactional
    public void trackFormAbandonment(String sessionId) {
        formAnalyticsRepository.findBySessionId(sessionId).ifPresent(analytics -> {
            if (analytics.getFormSubmittedAt() == null) {
                analytics.setFormAbandoned(true);
                formAnalyticsRepository.save(analytics);
                log.info("Form abandoned: sessionId={}", sessionId);
            }
        });
    }
    
    public Map<String, Object> getFormAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        analytics.put("totalFormStarts", formAnalyticsRepository.count());
        analytics.put("completedForms", formAnalyticsRepository.countCompletedForms());
        analytics.put("abandonedForms", formAnalyticsRepository.countAbandonedForms());
        analytics.put("averageCompletionTime", formAnalyticsRepository.getAverageTimeToComplete());
        
        Long completed = formAnalyticsRepository.countCompletedForms();
        Long total = formAnalyticsRepository.count();
        if (total > 0) {
            double conversionRate = (completed.doubleValue() / total.doubleValue()) * 100;
            analytics.put("conversionRate", String.format("%.1f%%", conversionRate));
        } else {
            analytics.put("conversionRate", "0%");
        }
        
        return analytics;
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
    
    private String detectDeviceType(String userAgent) {
        if (userAgent == null) return "Unknown";
        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("mobile") || userAgent.contains("android") || userAgent.contains("iphone")) {
            return "Mobile";
        } else if (userAgent.contains("tablet") || userAgent.contains("ipad")) {
            return "Tablet";
        }
        return "Desktop";
    }
}
