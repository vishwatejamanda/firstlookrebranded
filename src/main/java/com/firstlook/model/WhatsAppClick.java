package com.firstlook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Document(collection = "whatsapp_clicks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhatsAppClick {
    
    @Id
    private String id;
    
    private String ipAddress;
    private LocalDateTime clickedAt = LocalDateTime.now();
    private String sourcePage;
    private String userAgent;
}
