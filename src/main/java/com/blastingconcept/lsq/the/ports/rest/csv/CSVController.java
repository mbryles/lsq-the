package com.blastingconcept.lsq.the.ports.rest.csv;

import com.blastingconcept.lsq.the.domain.csv.CsvUploadService;

import com.blastingconcept.lsq.the.domain.csv.DuplicateSupplierKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/csv")
public class CSVController {


    private final CsvUploadService csvUploadService;

    public CSVController(CsvUploadService csvUploadService) {
        this.csvUploadService = csvUploadService;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) {
        String message = "";

        try {
            csvUploadService.load(file);
            message = "Uploaded file " + file.getOriginalFilename() + " successfully";
            return ResponseEntity.status(HttpStatus.OK).body(Payload.builder().message(message).build());
        } catch (DuplicateSupplierKeyException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Payload.builder()
                        .message(ex.getMessage())
                        .data(ex.getDuplicateRecords())
                        .build()
            );

        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Payload.builder()
                            .message(message)
                            .data(e)
                            .build()
            );
        }
    }
    
}
