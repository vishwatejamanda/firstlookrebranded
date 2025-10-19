package com.firstlook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "form_analytics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormAnalytics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "session_id")
    private String sessionId;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "form_started_at")
    private LocalDateTime formStartedAt;
    
    @Column(name = "form_submitted_at")
    private LocalDateTime formSubmittedAt;
    
    @Column(name = "time_spent_seconds")
    private Integer timeSpentSeconds;
    
    @Column(name = "field_interactions")
    private Integer fieldInteractions;
    
    @Column(name = "form_abandoned")
    private Boolean formAbandoned = false;
    
    @Column(name = "device_type")
    private String deviceType;
    
    @Column(name = "referrer_url")
    private String referrerUrl;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @OneToOne
    @JoinColumn(name = "contact_message_id")
    private ContactMessage contactMessage;
    
    @PrePersist
    protected void onCreate() {
        if (formStartedAt == null) {
            formStartedAt = LocalDateTime.now();
        }
    }
}
