package com.example.orderprocessingservice.controller;

import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Mono;

@RestController
public class ApplicationProcessingServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationProcessingServiceController.class);

    @Topic(name = "applications", pubsubName = "pubsub")
    @PostMapping(path = "/orders", consumes = MediaType.ALL_VALUE)
    public Mono<ResponseEntity> getCheckout(@RequestBody(required = false) CloudEvent<Application> cloudEvent) {
        return Mono.fromSupplier(() -> {
            logger.info("*********************************************************************************");
            logger.info("************************  DEMO Thu 8 09:35 a.m. *********************************");
            logger.info("*********************************************************************************");
            try {
                logger.info("_____>>      New application received to be validated : " +
                        cloudEvent.getData().getApplicationId() + "       <<_____");
                return ResponseEntity.ok("SUCCESS");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}

@Getter
@Setter
class Application {
    private int applicationId;
}