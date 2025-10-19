package com.firstlook.repository;

import com.firstlook.model.FormAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormAnalyticsRepository extends JpaRepository<FormAnalytics, Long> {
    
    Optional<FormAnalytics> findBySessionId(String sessionId);
    
    List<FormAnalytics> findByFormAbandonedTrue();
    
    @Query("SELECT AVG(f.timeSpentSeconds) FROM FormAnalytics f WHERE f.formSubmittedAt IS NOT NULL")
    Double getAverageTimeToComplete();
    
    @Query("SELECT COUNT(f) FROM FormAnalytics f WHERE f.formAbandoned = true")
    Long countAbandonedForms();
    
    @Query("SELECT COUNT(f) FROM FormAnalytics f WHERE f.formSubmittedAt IS NOT NULL")
    Long countCompletedForms();
    
    List<FormAnalytics> findByFormStartedAtBetween(LocalDateTime start, LocalDateTime end);
}
