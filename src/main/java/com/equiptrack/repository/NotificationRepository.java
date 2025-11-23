package com.equiptrack.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.equiptrack.model.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    
    List<Notification> findByUserId(String userId);
    
    List<Notification> findByUserIdAndIsReadFalse(String userId);
    
    @Query(value = "{ 'userId': ?0 }", sort = "{ 'createdAt': -1 }")
    List<Notification> findRecentNotificationsByUser(String userId);
    
    @Query(value = "{ 'userId': ?0, 'isRead': false }", count = true)
    Long countUnreadByUser(String userId);
    
    List<Notification> findByType(Notification.NotificationType type);
}
