package com.example.batchprocess.controller;

import com.example.batchprocess.entity.NotificationSticky;
import com.example.batchprocess.entity.NotificationStickyMongo;
import com.example.batchprocess.service.GenericTransferService;
import com.example.batchprocess.service.NotificationStickyService;
import com.mongodb.client.MongoCollection;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
public class BatchController {
    @Autowired
    private GenericTransferService transferService;
    private JobLauncher jobLauncher;
    private Job job;
    @Autowired
    private NotificationStickyService service;

    @GetMapping("/run-batch")
    public ResponseEntity runBatch() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        return ResponseEntity.ok().body("success");
    }

    @GetMapping(value = "/convert/object")
    ResponseEntity<?> TransferData() {
        CompletableFuture<List<NotificationSticky>> noti1 = service.findAllNotiByBatch(0,100000);
        CompletableFuture<List<NotificationSticky>> noti2 = service.findAllNotiByBatch(1,100000);
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(noti1, noti2);
        allFutures.join();
        try {
            List<NotificationSticky> result1 = noti1.get();
            List<NotificationSticky> result2 = noti2.get();

            List<List<NotificationSticky>> allNotifications = new ArrayList<>();
            allNotifications.add(result1);
            allNotifications.add(result2);
            /* Sau tạo nhiều process tùy theo batchsize*/
            for (List<NotificationSticky> stickyList : allNotifications) {
                List<NotificationStickyMongo> notificationStickyMongos =service.processEntities(stickyList) ;
                transferService.transferData(notificationStickyMongos,"notifications");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }
@GetMapping(value = "/convertv2/object")
ResponseEntity<?> TransferDatav2() throws ExecutionException, InterruptedException {
    int offset = 0;
    boolean check = true;
    while (check) {
        CompletableFuture<List<NotificationSticky>> notiFuture = service.findAllNotiByBatch(offset, 1000);
        List<NotificationSticky> notificationStickies = notiFuture.get();
        List<NotificationStickyMongo> notificationStickyMongos=service.processEntities(notificationStickies);
        transferService.transferData(notificationStickyMongos, "notifications");
        offset ++;
        if (notificationStickies.size() < 1000) {
            check = false;
        }
    }
    return ResponseEntity.status(HttpStatus.OK).body("Success");
}
}