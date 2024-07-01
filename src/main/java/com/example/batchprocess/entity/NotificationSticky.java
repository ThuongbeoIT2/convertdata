package com.example.batchprocess.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "notification_sticky")
@Table(name = "notification_sticky")
public class NotificationSticky {
    @Id
    private String uuid;
    private String id;
    private String type;
    private String nsType;
    private String userId;
    private String transId;
    private String requestTransId;
    private String title;
    private String message;
    private String urlDetail;
    private String notificationId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String viewStatus;
    private int priority;
    private String productCategoryCode;
    private String objectTypeDesc;
    private String objectValueDesc;
    private String goValue;
    private String description;
    private String productId;
    private String productCode;
    private String shortContent;
    private String thanked;
}