package com.blastingconcept.lsq.the.event;

import org.springframework.context.ApplicationEvent;


public class FileUploadedEvent extends ApplicationEvent{

    private static final long serialVersionUID = 1L;
    private final String message;

    public FileUploadedEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
}
