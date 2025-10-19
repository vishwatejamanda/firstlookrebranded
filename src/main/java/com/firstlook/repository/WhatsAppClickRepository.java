package com.firstlook.repository;

import com.firstlook.model.WhatsAppClick;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhatsAppClickRepository extends MongoRepository<WhatsAppClick, String> {
    
    Long countTotalClicks();
}
