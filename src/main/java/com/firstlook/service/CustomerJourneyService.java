package com.firstlook.service;

import com.firstlook.model.CustomerJourney;
import com.firstlook.repository.CustomerJourneyRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerJourneyService {
    
    private final CustomerJourneyRepository customerJourneyRepository;
    
    @Transactional
    public void trackEvent(String eventType, String eventData, HttpServletRequest request) {
        CustomerJourney journey = new CustomerJourney();
        journey.setIpAddress(getClientIp(request));
        journey.setSessionId(getOrCreateSessionId(request));
        journey.setEventType(eventType);
        journey.setEventData(eventData);
        journey.setPageUrl(request.getRequestURI());
        journey.setDeviceType(detectDeviceType(request.getHeader("User-Agent")));
        journey.setReferrer(request.getHeader("Referer"));
        
        customerJourneyRepository.save(journey);
        log.debug("Customer journey event tracked: type={}, ip={}", eventType, journey.getIpAddress());
    }
    
    public List<CustomerJourney> getJourneyByIp(String ipAddress) {
        return customerJourneyRepository.findByIpAddressOrderByTimestampAsc(ipAddress);
    }
    
    public List<CustomerJourney> getJourneyBySession(String sessionId) {
        return customerJourneyRepository.findBySessionIdOrderByTimestampAsc(sessionId);
    }
    
    public List<CustomerJourney> getRecentJourneys() {
        return customerJourneyRepository.findRecentEvents();
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
    
    private String getOrCreateSessionId(HttpServletRequest request) {
        // Try to get from session
        if (request.getSession(false) != null) {
            return request.getSession().getId();
        }
        return UUID.randomUUID().toString();
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
