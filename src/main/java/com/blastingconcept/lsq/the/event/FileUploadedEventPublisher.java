package com.blastingconcept.lsq.the.event;

import java.util.Date;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileUploadedEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public FileUploadedEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishFileUploadedEvent(final String message) {
        log.info("file uploaded at " + new Date());
        FileUploadedEvent event = new FileUploadedEvent(this, message);
        applicationEventPublisher.publishEvent(event);
    }
    
}
