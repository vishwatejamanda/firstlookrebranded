package com.firstlook.repository;

import com.firstlook.model.CustomerJourney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerJourneyRepository extends JpaRepository<CustomerJourney, Long> {
    
    List<CustomerJourney> findByIpAddressOrderByTimestampAsc(String ipAddress);
    
    List<CustomerJourney> findBySessionIdOrderByTimestampAsc(String sessionId);
    
    @Query("SELECT c.eventType, COUNT(c) FROM CustomerJourney c GROUP BY c.eventType")
    List<Object[]> getEventTypeDistribution();
    
    @Query("SELECT c FROM CustomerJourney c ORDER BY c.timestamp DESC")
    List<CustomerJourney> findRecentEvents();
}
