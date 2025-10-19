package com.firstlook.repository;

import com.firstlook.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    
    List<Visitor> findByVisitTimeBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT COUNT(v) FROM Visitor v")
    Long countTotalVisitors();
    
    @Query("SELECT COUNT(DISTINCT v.ipAddress) FROM Visitor v")
    Long countUniqueVisitors();
    
    @Query("SELECT v.country, COUNT(v) FROM Visitor v GROUP BY v.country ORDER BY COUNT(v) DESC")
    List<Object[]> getVisitorsByCountry();
    
    @Query("SELECT v.deviceType, COUNT(v) FROM Visitor v GROUP BY v.deviceType")
    List<Object[]> getVisitorsByDevice();
}
