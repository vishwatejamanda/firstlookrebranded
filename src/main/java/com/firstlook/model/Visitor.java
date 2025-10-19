package com.firstlook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visitor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "visit_time")
    private LocalDateTime visitTime;
    
    @Column(name = "page_visited")
    private String pageVisited;
    
    @Column(name = "referrer")
    private String referrer;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "device_type")
    private String deviceType;
    
    @Column(name = "browser")
    private String browser;
    
    @PrePersist
    protected void onCreate() {
        visitTime = LocalDateTime.now();
    }
}
