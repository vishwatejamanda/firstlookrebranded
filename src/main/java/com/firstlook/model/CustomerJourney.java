package com.firstlook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Document(collection = "customer_journey")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerJourney {
    
    @Id
    private String id;
    
    private String ipAddress;
    private String sessionId;
    private String eventType; // VISIT, FORM_START, FORM_SUBMIT, WHATSAPP_CLICK, PAGE_VIEW
    private String eventData;
    private String pageUrl;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String deviceType;
    private String referrer;
}
