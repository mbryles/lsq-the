package com.blastingconcept.lsq.the.domain.supplier;

import lombok.*;

@Data
@ToString
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class SupplierSummary {
    private String supplierId;
    private long totalInvoices;
    private long totalOpenInvoices;
    private long totalLateInvoices;
    private float totalOpenInvoiceAmount;
    private float totalLateInvoiceAmount;
}
