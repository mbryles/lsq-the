package com.blastingconcept.lsq.the.domain.csv;

import org.springframework.web.multipart.MultipartFile;

public interface CsvUploadService {

    void load(MultipartFile csvFile);
}
