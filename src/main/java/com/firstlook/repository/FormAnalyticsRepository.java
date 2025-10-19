package com.firstlook.repository;

import com.firstlook.model.FormAnalytics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormAnalyticsRepository extends MongoRepository<FormAnalytics, String> {
    
    Optional<FormAnalytics> findBySessionId(String sessionId);
    
    List<FormAnalytics> findByFormAbandonedTrue();
    
    Double getAverageTimeToComplete();
    
    Long countAbandonedForms();
    
    Long countCompletedForms();
    
    List<FormAnalytics> findByFormStartedAtBetween(LocalDateTime start, LocalDateTime end);
}
