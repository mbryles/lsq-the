package com.blastingconcept.lsq.the.domain.csv.impl;

import com.blastingconcept.lsq.the.domain.csv.CsvSupplier;
import com.blastingconcept.lsq.the.domain.csv.CsvUploadService;
import com.blastingconcept.lsq.the.domain.csv.InvoiceState;
import com.blastingconcept.lsq.the.domain.csv.Supplier;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
            this.csvToSupplier(csvFile.getInputStream()).stream()
                    .map(this::mapFromCsvSupplierAndApplyRules)
                    .collect(Collectors.toList()).stream()
                    .map(supplier -> this.supplierEntityRepository.save(this.mapFromSupplier(supplier)));

        } catch (IOException e) {
            throw new RuntimeException("failed to load csv data: " + e.getMessage());
        }

    }

    private List<CsvSupplier> csvToSupplier(InputStream in) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<CsvSupplier> suppliers = new ArrayList<CsvSupplier>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {


                CsvSupplier supplier = CsvSupplier.builder()
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

    private Supplier mapFromCsvSupplierAndApplyRules(CsvSupplier csvSupplier) {

        InvoiceState invoiceState = InvoiceState.NONE;

        if(csvSupplier.getPaymentAmount() > 0 && csvSupplier.getInvoiceAmount() > csvSupplier.getPaymentAmount()) {
            invoiceState = InvoiceState.OPEN;
        } else if (csvSupplier.getInvoiceAmount() >= csvSupplier.getPaymentAmount() &&
                csvSupplier.getPaymentDate().before(new Date())) {
            invoiceState = InvoiceState.CLOSED;
        } else if (csvSupplier.getPaymentAmount() == 0 && csvSupplier.getPaymentDate() == null  &&
                csvSupplier.getInvoiceDate().before(new Date())) {
            invoiceState = InvoiceState.LATE;
        } else if (csvSupplier.getPaymentDate() != null && csvSupplier.getPaymentDate().after(new Date())) {
            invoiceState = InvoiceState.PAYMENT_SCHEDULED;
        }


        return Supplier.builder()
                .supplierId(csvSupplier.getSupplierId())
                .invoiceId(csvSupplier.getInvoiceId())
                .invoiceAmount(csvSupplier.getInvoiceAmount())
                .terms(csvSupplier.getTerms())
                .paymentAmount(csvSupplier.getPaymentAmount())
                .paymentDate(csvSupplier.getPaymentDate())
                .invoiceDate(csvSupplier.getInvoiceDate())
                .invoiceState(invoiceState)
                .build();
    }

    private SupplierEntity mapFromSupplier(Supplier supplier) {

        SupplierEntityKey key = SupplierEntityKey.builder()
                .supplierId(supplier.getSupplierId())
                .invoiceId(supplier.getInvoiceId())
                .build();

        return SupplierEntity.builder()
                .id(key)
                .invoiceAmount(supplier.getInvoiceAmount())
                .terms(supplier.getTerms())
                .paymentAmount(supplier.getPaymentAmount())
                .paymentDate(supplier.getPaymentDate())
                .invoiceDate(supplier.getInvoiceDate())
                .invoiceState(supplier.getInvoiceState().name())
                .build();

    }
}
