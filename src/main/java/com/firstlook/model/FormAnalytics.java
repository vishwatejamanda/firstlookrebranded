package com.firstlook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Document(collection = "form_analytics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormAnalytics {
    
    @Id
    private String id;
    
    private String sessionId;
    private String ipAddress;
    private LocalDateTime formStartedAt = LocalDateTime.now();
    private LocalDateTime formSubmittedAt;
    private Integer timeSpentSeconds;
    private Integer fieldInteractions;
    private Boolean formAbandoned = false;
    private String deviceType;
    private String referrerUrl;
    private String userAgent;
    private String contactMessageId;
}
