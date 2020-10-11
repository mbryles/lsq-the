package com.blastingconcept.lsq.the.ports.rest.csv;

import com.blastingconcept.lsq.the.event.FileUploadedEventPublisher;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Component
@RestController("/api/csv")
public class CSVController {


    private final FileUploadedEventPublisher fileUploadedEventPublisher;

    public CSVController(FileUploadedEventPublisher fileUploadedEventPublisher) {
        this.fileUploadedEventPublisher = fileUploadedEventPublisher;
    }

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) {    
        return null;
    }
    
}
