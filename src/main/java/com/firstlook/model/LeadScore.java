package com.firstlook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "lead_scores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadScore {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "contact_message_id")
    private ContactMessage contactMessage;
    
    @Column(name = "score")
    private Integer score = 0;
    
    @Column(name = "quality")
    private String quality; // HOT, WARM, COLD
    
    @Column(name = "visited_before")
    private Boolean visitedBefore = false;
    
    @Column(name = "clicked_whatsapp")
    private Boolean clickedWhatsApp = false;
    
    @Column(name = "time_on_site_seconds")
    private Integer timeOnSiteSeconds;
    
    @Column(name = "pages_visited")
    private Integer pagesVisited = 1;
    
    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;
    
    @PrePersist
    protected void onCreate() {
        calculatedAt = LocalDateTime.now();
        calculateQuality();
    }
    
    @PreUpdate
    protected void onUpdate() {
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
