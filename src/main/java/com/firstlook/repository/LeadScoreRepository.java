package com.firstlook.repository;

import com.firstlook.model.LeadScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeadScoreRepository extends JpaRepository<LeadScore, Long> {
    
    Optional<LeadScore> findByContactMessageId(Long contactMessageId);
    
    List<LeadScore> findByQuality(String quality);
    
    List<LeadScore> findByScoreGreaterThanEqualOrderByScoreDesc(Integer minScore);
    
    @Query("SELECT l.quality, COUNT(l) FROM LeadScore l GROUP BY l.quality")
    List<Object[]> getLeadQualityDistribution();
    
    @Query("SELECT AVG(l.score) FROM LeadScore l")
    Double getAverageLeadScore();
}
