package com.firstlook.repository;

import com.firstlook.model.Visitor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitorRepository extends MongoRepository<Visitor, String> {
    
    List<Visitor> findByVisitTimeBetween(LocalDateTime start, LocalDateTime end);
    
    Long countTotalVisitors();
    
    Long countUniqueVisitors();
    
    List<Object[]> getVisitorsByCountry();
    
    List<Object[]> getVisitorsByDevice();
}
