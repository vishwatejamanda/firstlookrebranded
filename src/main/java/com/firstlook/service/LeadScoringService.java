package com.firstlook.service;

import com.firstlook.model.ContactMessage;
import com.firstlook.model.LeadScore;
import com.firstlook.repository.CustomerJourneyRepository;
import com.firstlook.repository.LeadScoreRepository;
import com.firstlook.repository.VisitorRepository;
import com.firstlook.repository.WhatsAppClickRepository;
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
public class LeadScoringService {
    
    private final LeadScoreRepository leadScoreRepository;
    private final VisitorRepository visitorRepository;
    private final WhatsAppClickRepository whatsAppClickRepository;
    private final CustomerJourneyRepository customerJourneyRepository;
    
    @Transactional
    public LeadScore calculateLeadScore(ContactMessage contactMessage) {
        LeadScore leadScore = new LeadScore();
        leadScore.setContactMessageId(contactMessage.getId());
        
        int score = 0;
        String ipAddress = contactMessage.getIpAddress();
        
        // Base score for submitting form
        score += 20;
        
        // Check if visited before
        long visitCount = visitorRepository.findAll().stream()
            .filter(v -> v.getIpAddress().equals(ipAddress))
            .count();
        
        if (visitCount > 1) {
            leadScore.setVisitedBefore(true);
            score += 15;
            leadScore.setPagesVisited((int) visitCount);
        }
        
        // Check if clicked WhatsApp
        boolean clickedWhatsApp = whatsAppClickRepository.findAll().stream()
            .anyMatch(w -> w.getIpAddress().equals(ipAddress));
        
        if (clickedWhatsApp) {
            leadScore.setClickedWhatsApp(true);
            score += 25;
        }
        
        // Score based on message length (shows engagement)
        if (contactMessage.getMessage() != null) {
            int messageLength = contactMessage.getMessage().length();
            if (messageLength > 200) {
                score += 15;
            } else if (messageLength > 100) {
                score += 10;
            } else if (messageLength > 50) {
                score += 5;
            }
        }
        
        // Score based on subject specificity
        if (contactMessage.getSubject() != null && contactMessage.getSubject().length() > 10) {
            score += 10;
        }
        
        // Check customer journey events
        long journeyEvents = customerJourneyRepository.findByIpAddressOrderByTimestampAsc(ipAddress).size();
        if (journeyEvents > 5) {
            score += 15;
        } else if (journeyEvents > 3) {
            score += 10;
        }
        
        leadScore.setScore(score);
        
        LeadScore saved = leadScoreRepository.save(leadScore);
        log.info("Lead score calculated: contactId={}, score={}, quality={}", 
            contactMessage.getId(), score, saved.getQuality());
        
        return saved;
    }
    
    public Map<String, Object> getLeadScoringAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        List<LeadScore> allLeads = leadScoreRepository.findAll();
        
        long hotLeads = allLeads.stream().filter(l -> "HOT".equals(l.getQuality())).count();
        long warmLeads = allLeads.stream().filter(l -> "WARM".equals(l.getQuality())).count();
        long coldLeads = allLeads.stream().filter(l -> "COLD".equals(l.getQuality())).count();
        
        double averageScore = allLeads.stream()
            .mapToInt(LeadScore::getScore)
            .average()
            .orElse(0.0);
        
        Map<String, Long> qualityMap = new HashMap<>();
        qualityMap.put("HOT", hotLeads);
        qualityMap.put("WARM", warmLeads);
        qualityMap.put("COLD", coldLeads);
        
        analytics.put("hotLeads", hotLeads);
        analytics.put("warmLeads", warmLeads);
        analytics.put("coldLeads", coldLeads);
        analytics.put("averageScore", averageScore);
        analytics.put("qualityDistribution", qualityMap);
        
        return analytics;
    }
    
    public List<LeadScore> getHotLeads() {
        return leadScoreRepository.findByQuality("HOT");
    }
}
