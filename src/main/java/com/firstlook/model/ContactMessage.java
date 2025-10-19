package com.firstlook.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Document(collection = "contact_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessage {
    
    @Id
    private String id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    private String mobile;
    
    @NotBlank(message = "Subject is required")
    private String subject;
    
    @NotBlank(message = "Message is required")
    private String message;
    
    private LocalDateTime submittedAt = LocalDateTime.now();
    
    private String status = "ON_HOLD";
    
    private String ipAddress;
    
    private Boolean catalogOpened = false;
    
    private LocalDateTime catalogOpenedAt;
    
    private Boolean catalogMessageSent = false;
    
    private Boolean catalogSendFailed = false;
    
    private String catalogFailureReason;
}
