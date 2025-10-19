package com.firstlook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "mobile")
    private String mobile;
    
    @NotBlank(message = "Subject is required")
    @Column(name = "subject", nullable = false)
    private String subject;
    
    @NotBlank(message = "Message is required")
    @Column(name = "message", nullable = false, length = 2000)
    private String message;
    
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    @Column(name = "status")
    private String status = "ON_HOLD";
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "catalog_opened")
    private Boolean catalogOpened = false;
    
    @Column(name = "catalog_opened_at")
    private LocalDateTime catalogOpenedAt;
    
    @Column(name = "catalog_message_sent")
    private Boolean catalogMessageSent = false;
    
    @Column(name = "catalog_send_failed")
    private Boolean catalogSendFailed = false;
    
    @Column(name = "catalog_failure_reason")
    private String catalogFailureReason;
    
    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }
}
