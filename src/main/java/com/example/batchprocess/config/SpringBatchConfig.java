package com.example.batchprocess.config;

import com.example.batchprocess.entity.NotificationSticky;
import com.example.batchprocess.entity.NotificationStickyMongo;
import com.example.batchprocess.repository.NotificationStickyRepo;
import com.example.batchprocess.repository.NotificationStickyMongoRepo;
import com.mongodb.client.MongoDatabase;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;

import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;


import org.springframework.batch.item.support.CompositeItemWriter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.util.Arrays;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    private NotificationStickyMongoRepo notificationStickyMongoRepo;
    private EntityManagerFactory entityManagerFactory;
    private final MongoDatabase mongoDatabase= MongoDBConfig.getDatabase("data");
    @Bean
    public JpaPagingItemReader<NotificationSticky> reader() {
        JpaPagingItemReader<NotificationSticky> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT n FROM notification_sticky n");
        reader.setPageSize(10000);
        return reader;
    }

    @Bean
    public ItemProcessor<NotificationSticky, NotificationStickyMongo> processor() {

        return notificationSticky -> {
            NotificationStickyMongo mongo = new NotificationStickyMongo();
            mongo.setUuid(notificationSticky.getUuid());
            mongo.setId(notificationSticky.getId());
            mongo.setType(notificationSticky.getType());
            mongo.setNsType(notificationSticky.getNsType());
            mongo.setUserId(notificationSticky.getUserId());
            mongo.setTransId(notificationSticky.getTransId());
            mongo.setRequestTransId(notificationSticky.getRequestTransId());
            mongo.setTitle(notificationSticky.getTitle());
            mongo.setMessage(notificationSticky.getMessage());
            mongo.setUrlDetail(notificationSticky.getUrlDetail());
            mongo.setNotificationId(notificationSticky.getNotificationId());
            mongo.setStartTime(notificationSticky.getStartTime().toString());
            mongo.setEndTime(notificationSticky.getEndTime().toString());
            mongo.setStatus(notificationSticky.getStatus());
            mongo.setViewStatus(notificationSticky.getViewStatus());
            mongo.setPriority(notificationSticky.getPriority());
            mongo.setProductCategoryCode(notificationSticky.getProductCategoryCode());
            mongo.setObjectTypeDesc(notificationSticky.getObjectTypeDesc());
            mongo.setObjectValueDesc(notificationSticky.getObjectValueDesc());
            mongo.setGoValue(notificationSticky.getGoValue());
            mongo.setDescription(notificationSticky.getDescription());
            mongo.setProductId(notificationSticky.getProductId());
            mongo.setProductCode(notificationSticky.getProductCode());
            mongo.setShortContent(notificationSticky.getShortContent());
            mongo.setThanked(notificationSticky.getThanked());

            return mongo;
        };
    }

    @Bean
    public FlatFileItemWriter<NotificationStickyMongo> fileWriter() {
        FlatFileItemWriter<NotificationStickyMongo> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("output/notificationSticky.csv"));
        writer.setAppendAllowed(true);
        writer.setLineAggregator(new DelimitedLineAggregator<NotificationStickyMongo>() {{
            setDelimiter(",");
            setFieldExtractor(new FieldExtractor<NotificationStickyMongo>() {
                @Override
                public Object[] extract(NotificationStickyMongo item) {
                    return new Object[]{
                            item.getUuid(),
                            item.getId(),
                            item.getType(),
                            item.getNsType(),
                            item.getUserId(),
                            item.getTransId(),
                            item.getRequestTransId(),
                            item.getTitle(),
                            item.getMessage(),
                            item.getUrlDetail(),
                            item.getNotificationId(),
                            item.getStartTime(),
                            item.getEndTime(),
                            item.getStatus(),
                            item.getViewStatus(),
                            item.getPriority(),
                            item.getProductCategoryCode(),
                            item.getObjectTypeDesc(),
                            item.getObjectValueDesc(),
                            item.getGoValue(),
                            item.getDescription(),
                            item.getProductId(),
                            item.getProductCode(),
                            item.getShortContent(),
                            item.getThanked()
                    };
                }
            });
        }});
        return writer;
    }

    @Bean
    public RepositoryItemWriter<NotificationStickyMongo> mongoWriter() {
        RepositoryItemWriter<NotificationStickyMongo> writer = new RepositoryItemWriter<>();
        writer.setRepository(notificationStickyMongoRepo);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public CompositeItemWriter<NotificationStickyMongo> compositeWriter() {
        CompositeItemWriter<NotificationStickyMongo> writer = new CompositeItemWriter<>();
        writer.setDelegates(Arrays.asList(fileWriter(), mongoWriter()));
        return writer;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<NotificationSticky, NotificationStickyMongo>chunk(10000)
                .reader(reader())
                .processor(processor())
                .writer(compositeWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job exportUserJob() {
        return jobBuilderFactory.get("exportUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

}
