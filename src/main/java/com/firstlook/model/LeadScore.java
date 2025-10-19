package com.firstlook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Document(collection = "lead_scores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadScore {
    
    @Id
    private String id;
    
    private String contactMessageId;
    
    private Integer score = 0;
    private String quality; // HOT, WARM, COLD
    private Boolean visitedBefore = false;
    private Boolean clickedWhatsApp = false;
    private Integer timeOnSiteSeconds;
    private Integer pagesVisited = 1;
    private LocalDateTime calculatedAt = LocalDateTime.now();
    
    public void updateQuality() {
        calculateQuality();
    }
    
    private void calculateQuality() {
        if (score >= 70) {
            quality = "HOT";
        } else if (score >= 40) {
            quality = "WARM";
        } else {
            quality = "COLD";
        }
    }
}
