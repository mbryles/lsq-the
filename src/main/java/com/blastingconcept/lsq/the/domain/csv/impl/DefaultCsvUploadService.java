package com.blastingconcept.lsq.the.domain.csv.impl;

import com.blastingconcept.lsq.the.domain.csv.CsvSupplier;
import com.blastingconcept.lsq.the.domain.csv.CsvUploadService;
import com.blastingconcept.lsq.the.domain.supplier.Supplier;
import com.blastingconcept.lsq.the.ports.persistence.supplier.SupplierEntity;
import com.blastingconcept.lsq.the.ports.persistence.supplier.SupplierEntityKey;
import com.blastingconcept.lsq.the.ports.persistence.supplier.SupplierEntityRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DefaultCsvUploadService implements CsvUploadService {

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy", Locale.ENGLISH);

    private final SupplierEntityRepository supplierEntityRepository;

    public DefaultCsvUploadService(SupplierEntityRepository supplierEntityRepository) {
        this.supplierEntityRepository = supplierEntityRepository;
    }

    @Override
    public void load(MultipartFile csvFile) {
        try {
            List<Supplier> suppliers = this.csvToSupplier(csvFile.getInputStream());



        } catch (IOException e) {
            throw new RuntimeException("failed to load csv data: " + e.getMessage());
        }

    }

    private List<Supplier> csvToSupplier(InputStream in) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Supplier> suppliers = new ArrayList<Supplier>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {

                Supplier supplier = Supplier.builder()
                        .supplierId(csvRecord.get("Supplier Id"))
                        .invoiceId(csvRecord.get("Invoice Id"))
                        .invoiceAmount(Float.parseFloat(csvRecord.get("Invoice Amount")))
                        .terms(Integer.parseInt(csvRecord.get("Terms")))
                        .paymentAmount(Float.parseFloat(csvRecord.get("Payment Amount")))
                        .paymentDate(formatter.parse(csvRecord.get("Payment Date")))
                        .invoiceDate(formatter.parse(csvRecord.get("Invoice Date")))
                        .build();

                suppliers.add(supplier);
            }

            return suppliers;
        } catch (IOException | ParseException e) {
            throw new RuntimeException("failed to parse CSV file: " + e.getMessage());
        }
    }
}
