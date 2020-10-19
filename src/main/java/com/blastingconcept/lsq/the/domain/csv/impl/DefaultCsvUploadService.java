package com.blastingconcept.lsq.the.domain.csv.impl;

import com.blastingconcept.lsq.the.domain.csv.*;
import com.blastingconcept.lsq.the.domain.supplier.Supplier;
import com.blastingconcept.lsq.the.ports.persistence.supplier.SupplierEntity;
import com.blastingconcept.lsq.the.ports.persistence.supplier.SupplierEntityKey;
import com.blastingconcept.lsq.the.ports.persistence.supplier.SupplierEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DefaultCsvUploadService implements CsvUploadService {

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private final SupplierEntityRepository supplierEntityRepository;

    public DefaultCsvUploadService(SupplierEntityRepository supplierEntityRepository) {
        this.supplierEntityRepository = supplierEntityRepository;
    }


    @Transactional
            (rollbackFor = Exception.class,
                    noRollbackFor = EntityNotFoundException.class)
    @Override
    public void load(MultipartFile csvFile) {
        try {
             this.csvToSupplier(csvFile.getInputStream())
                     .forEach(csvSupplier -> {
                        Supplier supplier = this.mapFromCsvSupplierAndApplyRules(csvSupplier);
                        this.supplierEntityRepository.save(this.mapFromSupplier(supplier));
                     });

        } catch (IOException e) {
            throw new RuntimeException("failed to load csv data: " + e.getMessage());
        }

    }

    private List<CsvSupplier> csvToSupplier(InputStream in) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<CsvSupplier> suppliers = new ArrayList<CsvSupplier>();

            Map<SupplierEntityKey,Integer> duplicatesMap = new HashMap<>();
            Set<CSVRecord> records = new HashSet<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {

                String supplierId = csvRecord.get("Supplier Id");
                String invoiceId = csvRecord.get("Invoice Id");

                SupplierEntityKey key = SupplierEntityKey.builder()
                        .supplierId(supplierId)
                        .invoiceId(invoiceId)
                        .build();

                Integer i = duplicatesMap.get(key);
                if(i != null) {
                    duplicatesMap.put(key, ++i);
                } else {
                    duplicatesMap.put(key, 1);
                }

                String invoiceAmount = csvRecord.get("Invoice Amount");
                float invoiceAmt = 0.0f;
                if(invoiceAmount != null && ! invoiceAmount.isEmpty()) {
                    invoiceAmt = Float.parseFloat(invoiceAmount);
                }

                String paymentAmount = csvRecord.get("Payment Amount");
                float paymentAmt = 0.0f;

                if(paymentAmount != null && ! paymentAmount.isEmpty()) {
                    paymentAmt = Float.parseFloat(paymentAmount);
                }

                String paymentDate =csvRecord.get("Payment Date");
                Date paymentDt = null;

                if(paymentDate != null && !paymentDate.isEmpty()) {
                    paymentDt = formatter.parse(paymentDate);
                }

                String invoiceDate =csvRecord.get("Invoice Date");
                Date invoiceDt = null;

                if(invoiceDate != null && !invoiceDate.isEmpty()) {
                    invoiceDt = formatter.parse(invoiceDate);
                }

                CsvSupplier supplier = CsvSupplier.builder()
                        .supplierId(csvRecord.get("Supplier Id"))
                        .invoiceId(csvRecord.get("Invoice Id"))
                        .invoiceAmount(invoiceAmt)
                        .terms(Integer.parseInt(csvRecord.get("Terms")))
                        .paymentAmount(paymentAmt)
                        .paymentDate(paymentDt)
                        .invoiceDate(invoiceDt)
                        .build();

                suppliers.add(supplier);
            }

            log.debug(duplicatesMap.keySet().toString());

            if(duplicatesMap.keySet().size() != suppliers.size()) {
                log.debug("not good");

                List<SupplierEntityKey> duplicateKeys = duplicatesMap.entrySet().stream()
                        .filter(supplierEntityKeyIntegerEntry -> supplierEntityKeyIntegerEntry.getValue() > 1)
                        .map(supplierEntityKeyIntegerEntry -> supplierEntityKeyIntegerEntry.getKey())
                        .collect(Collectors.toList());

                throw new DuplicateSupplierKeyException("duplicate keys detected", duplicateKeys);
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
        } else if (csvSupplier.getInvoiceAmount() >= csvSupplier.getPaymentAmount() && csvSupplier.getPaymentDate() != null &&
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

        SupplierEntity supplierEntity = new SupplierEntity();
        supplierEntity.setId(key);
        supplierEntity.setInvoiceAmount(supplier.getInvoiceAmount());
        supplierEntity.setTerms(supplier.getTerms());
        supplierEntity.setPaymentAmount(supplier.getPaymentAmount());
        supplierEntity.setPaymentDate(supplier.getPaymentDate());
        supplierEntity.setInvoiceDate(supplier.getInvoiceDate());
        supplierEntity.setInvoiceState(supplier.getInvoiceState().name());

        return supplierEntity;

    }
}
