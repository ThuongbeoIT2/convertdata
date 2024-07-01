package com.example.batchprocess.service;

import com.example.batchprocess.entity.NotificationSticky;
import com.example.batchprocess.entity.NotificationStickyMongo;
import com.example.batchprocess.repository.NotificationStickyRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class NotificationStickyService {
    @Autowired
    private NotificationStickyRepo repo;
    Object target;
    Logger logger = LoggerFactory.getLogger(NotificationStickyService.class);
    public CompletableFuture<List<NotificationSticky>> findAllNotiByBatch(int batch, int size) {
        return CompletableFuture.supplyAsync(() -> {
            Pageable pageable = PageRequest.of(batch, size);
            return repo.findAllNotifications(pageable).getContent();
        });
    }
    @Async
    public CompletableFuture<List<NotificationSticky>> saveNoti(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        List<NotificationSticky> notificationStickies = parseCSVFile(file);
        logger.info("saving list of noti of size {}", notificationStickies.size(), "" + Thread.currentThread().getName());
        notificationStickies = repo.saveAll(notificationStickies);
        long end = System.currentTimeMillis();
        logger.info("Total time {}", (end - start));
        return CompletableFuture.completedFuture(notificationStickies);
    }
    @Async
    public CompletableFuture<List<NotificationSticky>> findAllNoti(){
        logger.info("get list of noti by "+Thread.currentThread().getName());
        List<NotificationSticky> notificationStickies=repo.findAll();
        return CompletableFuture.completedFuture(notificationStickies);
    }
    public  List<NotificationStickyMongo> processEntities(List<NotificationSticky> entities) {
        List<NotificationStickyMongo> processedEntities =entities.stream().map(notificationSticky -> {
            NotificationStickyMongo notification= new NotificationStickyMongo(notificationSticky);
            return notification;
        }).collect(Collectors.toList());
        return processedEntities;
    }
    private List<NotificationSticky> parseCSVFile(final MultipartFile file) throws Exception {
        final List<NotificationSticky> notificationStickies = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                    NotificationSticky notificationSticky = new NotificationSticky();
                    notificationSticky.setUuid(data[0]);
                    notificationSticky.setId(data[1]);
                    notificationSticky.setType(data[2]);
                    notificationSticky.setNsType(data[3]);
                    notificationSticky.setUserId(data[4]);
                    notificationSticky.setTransId(data[5]);
                    notificationSticky.setRequestTransId(data[6]);
                    notificationSticky.setTitle(data[7]);
                    notificationSticky.setMessage(data[8]);
                    notificationSticky.setUrlDetail(data[9]);
                    notificationSticky.setNotificationId(data[10]);
                    notificationSticky.setStartTime(LocalDateTime.parse(data[11], formatter));
                    notificationSticky.setEndTime(LocalDateTime.parse(data[12], formatter));
                    notificationSticky.setStatus(data[13]);
                    notificationSticky.setViewStatus(data[14]);
                    notificationSticky.setPriority(Integer.parseInt(data[15]));
                    notificationSticky.setProductCategoryCode(data[16]);
                    notificationSticky.setObjectTypeDesc(data[17]);
                    notificationSticky.setObjectValueDesc(data[18]);
                    notificationSticky.setGoValue(data[19]);
                    notificationSticky.setDescription(data[20]);
                    notificationSticky.setProductId(data[21]);
                    notificationSticky.setProductCode(data[22]);
                    notificationSticky.setShortContent(data[23]);
                    notificationSticky.setThanked(data[24]);
                    notificationStickies.add(notificationSticky);
                }
                return notificationStickies;
            }
        } catch (final IOException e) {
            logger.error("Failed to parse CSV file {}", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }
}
