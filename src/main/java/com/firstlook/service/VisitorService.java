package com.firstlook.service;

import com.firstlook.model.Visitor;
import com.firstlook.repository.VisitorRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class VisitorService {
    
    private final VisitorRepository visitorRepository;
    
    @Transactional
    public void trackVisitor(HttpServletRequest request, String page) {
        try {
            Visitor visitor = new Visitor();
            visitor.setIpAddress(getClientIp(request));
            visitor.setUserAgent(request.getHeader("User-Agent"));
            visitor.setPageVisited(page);
            visitor.setReferrer(request.getHeader("Referer"));
            visitor.setDeviceType(detectDeviceType(request.getHeader("User-Agent")));
            visitor.setBrowser(detectBrowser(request.getHeader("User-Agent")));
            
            visitorRepository.save(visitor);
            log.info("Visitor tracked: IP={}, Page={}", visitor.getIpAddress(), page);
        } catch (Exception e) {
            log.error("Error tracking visitor", e);
        }
    }
    
    public Map<String, Object> getAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalVisitors", visitorRepository.countTotalVisitors());
        analytics.put("uniqueVisitors", visitorRepository.countUniqueVisitors());
        analytics.put("visitorsByCountry", visitorRepository.getVisitorsByCountry());
        analytics.put("visitorsByDevice", visitorRepository.getVisitorsByDevice());
        return analytics;
    }
    
    public List<Visitor> getRecentVisitors(int limit) {
        return visitorRepository.findAll()
                .stream()
                .sorted((v1, v2) -> v2.getVisitTime().compareTo(v1.getVisitTime()))
                .limit(limit)
                .toList();
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
    
    private String detectBrowser(String userAgent) {
        if (userAgent == null) return "Unknown";
        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("chrome")) return "Chrome";
        if (userAgent.contains("firefox")) return "Firefox";
        if (userAgent.contains("safari")) return "Safari";
        if (userAgent.contains("edge")) return "Edge";
        if (userAgent.contains("opera")) return "Opera";
        return "Other";
    }
}
