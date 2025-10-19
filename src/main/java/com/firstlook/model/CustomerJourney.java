package com.firstlook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_journey")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerJourney {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "session_id")
    private String sessionId;
    
    @Column(name = "event_type")
    private String eventType; // VISIT, FORM_START, FORM_SUBMIT, WHATSAPP_CLICK, PAGE_VIEW
    
    @Column(name = "event_data", length = 1000)
    private String eventData;
    
    @Column(name = "page_url")
    private String pageUrl;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @Column(name = "device_type")
    private String deviceType;
    
    @Column(name = "referrer")
    private String referrer;
    
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
