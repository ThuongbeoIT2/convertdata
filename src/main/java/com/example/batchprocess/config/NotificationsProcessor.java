package com.example.batchprocess.config;

import com.example.batchprocess.entity.NotificationSticky;
import org.springframework.batch.item.ItemProcessor;

public class NotificationsProcessor implements ItemProcessor<NotificationSticky, NotificationSticky> {
    @Override
    public NotificationSticky process(NotificationSticky notificationSticky) throws Exception {
        return notificationSticky;
    }
}