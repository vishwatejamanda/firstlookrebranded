package com.firstlook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Document(collection = "visitors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visitor {
    
    @Id
    private String id;
    
    private String ipAddress;
    private String userAgent;
    private LocalDateTime visitTime = LocalDateTime.now();
    private String pageVisited;
    private String referrer;
    private String country;
    private String city;
    private String deviceType;
    private String browser;
}
