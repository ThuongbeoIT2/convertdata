package com.example.batchprocess.repository;


import com.example.batchprocess.entity.NotificationSticky;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationStickyRepo extends JpaRepository<NotificationSticky,String> {
    @Query("select o from notification_sticky o")
    Page<NotificationSticky> findAllNotifications(Pageable pageable);
}
