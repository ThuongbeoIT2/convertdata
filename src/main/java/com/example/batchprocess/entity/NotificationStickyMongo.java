package com.example.batchprocess.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notification_sticky")
public class NotificationStickyMongo {
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
    private String startTime;
    private String endTime;
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
    public NotificationStickyMongo(NotificationSticky notificationSticky) {
        this.uuid = notificationSticky.getUuid();
        this.id = notificationSticky.getId();
        this.type = notificationSticky.getType();
        this.nsType = notificationSticky.getNsType();
        this.userId = notificationSticky.getUserId();
        this.transId = notificationSticky.getTransId();
        this.requestTransId = notificationSticky.getRequestTransId();
        this.title = notificationSticky.getTitle();
        this.message = notificationSticky.getMessage();
        this.urlDetail = notificationSticky.getUrlDetail();
        this.notificationId = notificationSticky.getNotificationId();
        this.startTime = notificationSticky.getStartTime().toString();
        this.endTime = notificationSticky.getEndTime().toString();
        this.status = notificationSticky.getStatus();
        this.viewStatus = notificationSticky.getViewStatus();
        this.priority = notificationSticky.getPriority();
        this.productCategoryCode = notificationSticky.getProductCategoryCode();
        this.objectTypeDesc = notificationSticky.getObjectTypeDesc();
        this.objectValueDesc = notificationSticky.getObjectValueDesc();
        this.goValue = notificationSticky.getGoValue();
        this.description = notificationSticky.getDescription();
        this.productId = notificationSticky.getProductId();
        this.productCode = notificationSticky.getProductCode();
        this.shortContent = notificationSticky.getShortContent();
        this.thanked = notificationSticky.getThanked();

    }
}
