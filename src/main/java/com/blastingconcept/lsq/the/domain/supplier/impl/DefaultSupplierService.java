package com.blastingconcept.lsq.the.domain.supplier.impl;

import com.blastingconcept.lsq.the.domain.csv.InvoiceState;
import com.blastingconcept.lsq.the.domain.supplier.Supplier;
import com.blastingconcept.lsq.the.domain.supplier.SupplierService;
import com.blastingconcept.lsq.the.domain.supplier.SupplierSummary;
import com.blastingconcept.lsq.the.ports.persistence.supplier.SupplierEntity;
import com.blastingconcept.lsq.the.ports.persistence.supplier.SupplierEntityRepository;
import com.blastingconcept.lsq.the.ports.persistence.supplier.SupplierSummaryView;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DefaultSupplierService implements SupplierService {

    private final SupplierEntityRepository supplierEntityRepository;

    public DefaultSupplierService(SupplierEntityRepository supplierEntityRepository ) {
        this.supplierEntityRepository = supplierEntityRepository;
    }

    @Override
    public List<SupplierSummary> getAllSupplierSummaries() {

//        Map<String, List<SupplierEntity>> supplierIdMap = StreamSupport.stream(this.supplierEntityRepository.findAll().spliterator(), false)
//                .collect(Collectors.toList()).stream()
//                .collect(Collectors.groupingBy(SupplierEntity::getSupplierId));


        Map<String, SupplierSummary> supplierSummaryMap = new HashMap<>();

        List<SupplierSummaryView> supplierSummaryEntities = this.supplierEntityRepository.findAllSupplierSummaries();

        supplierSummaryEntities.stream().forEach(supplierSummaryView -> {

            if(!supplierSummaryMap.containsKey(supplierSummaryView.getSupplierId())) {

                SupplierSummary.SupplierSummaryBuilder builder = SupplierSummary.builder();
                builder.supplierId(supplierSummaryView.getSupplierId());
                builder.totalInvoices(supplierSummaryView.getInvoiceTotal());

                if(supplierSummaryView.getInvoiceState().equalsIgnoreCase("OPEN")) {
                    builder.totalOpenInvoiceAmount(supplierSummaryView.getInvoiceAmountSum());
                    builder.totalOpenInvoices(supplierSummaryView.getInvoiceIdCountByState());
                }

                if(supplierSummaryView.getInvoiceState().equalsIgnoreCase("LATE")) {
                    builder.totalLateInvoiceAmount(supplierSummaryView.getInvoiceAmountSum());
                    builder.totalLateInvoices(supplierSummaryView.getInvoiceIdCountByState());
                }


                supplierSummaryMap.put(supplierSummaryView.getSupplierId(),builder.build());
            } else {

                SupplierSummary supplierSummary = supplierSummaryMap.get(supplierSummaryView.getSupplierId());

                Long totalInvoices = supplierSummary.getTotalInvoices() + supplierSummaryView.getInvoiceTotal();




                SupplierSummary.SupplierSummaryBuilder builder = supplierSummary.toBuilder();
//                builder.supplierId(supplierSummaryView.getSupplierId());
                builder.totalInvoices(supplierSummary.getTotalInvoices() + supplierSummaryView.getInvoiceTotal());

                if(supplierSummaryView.getInvoiceState().equalsIgnoreCase("OPEN")) {
                    builder.totalOpenInvoiceAmount(supplierSummary.getTotalOpenInvoiceAmount() +
                            supplierSummaryView.getInvoiceAmountSum());
                    builder.totalOpenInvoices(supplierSummary.getTotalOpenInvoices() + supplierSummaryView.getInvoiceIdCountByState());
                }

                if(supplierSummaryView.getInvoiceState().equalsIgnoreCase("LATE")) {
                    builder.totalLateInvoiceAmount(supplierSummary.getTotalLateInvoiceAmount() + supplierSummaryView.getInvoiceAmountSum());
                    builder.totalLateInvoices(supplierSummary.getTotalLateInvoices() + supplierSummaryView.getInvoiceIdCountByState());
                }


                supplierSummaryMap.put(supplierSummaryView.getSupplierId(),builder.build());

            }


        });

        List<SupplierSummary> supplierSummaries = new ArrayList<>();

        return new ArrayList<>(supplierSummaryMap.values());
    }

    @Override
    public List<Supplier> getSupplierById(String id) {

        List<SupplierEntity> entities =this.supplierEntityRepository.findBySupplierId(id);

        List<Supplier> suppliers = entities.stream()
                .map(supplierEntity -> Supplier.builder()
                        .supplierId(supplierEntity.getSupplierId())
                        .invoiceId(supplierEntity.getInvoiceId())
                        .invoiceState(InvoiceState.valueOf(supplierEntity.getInvoiceState()))
                        .invoiceAmount(supplierEntity.getInvoiceAmount())
                        .invoiceDate(supplierEntity.getInvoiceDate())
                        .paymentAmount(supplierEntity.getPaymentAmount())
                        .paymentDate(supplierEntity.getPaymentDate())
                        .terms(supplierEntity.getTerms())
                        .build()
                ).collect(Collectors.toList());

        return suppliers;
    }
}
