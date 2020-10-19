package com.blastingconcept.lsq.the.domain.csv;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CsvUploadService {

    void load(MultipartFile csvFile) throws IOException;
}
