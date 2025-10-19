package com.firstlook.repository;

import com.firstlook.model.LeadScore;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeadScoreRepository extends MongoRepository<LeadScore, String> {
    
    Optional<LeadScore> findByContactMessageId(String contactMessageId);
    
    List<LeadScore> findByQuality(String quality);
    
    List<LeadScore> findByScoreGreaterThanEqualOrderByScoreDesc(Integer minScore);
}
