package com.firstlook.repository;

import com.firstlook.model.WhatsAppClick;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WhatsAppClickRepository extends JpaRepository<WhatsAppClick, Long> {
    
    @Query("SELECT COUNT(w) FROM WhatsAppClick w")
    Long countTotalClicks();
}
