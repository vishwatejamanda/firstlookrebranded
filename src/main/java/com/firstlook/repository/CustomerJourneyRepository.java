package com.firstlook.repository;

import com.firstlook.model.CustomerJourney;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerJourneyRepository extends MongoRepository<CustomerJourney, String> {
    
    List<CustomerJourney> findByIpAddressOrderByTimestampAsc(String ipAddress);
    
    List<CustomerJourney> findBySessionIdOrderByTimestampAsc(String sessionId);
    
    List<Object[]> getEventTypeDistribution();
    
    List<CustomerJourney> findRecentEvents();
}
