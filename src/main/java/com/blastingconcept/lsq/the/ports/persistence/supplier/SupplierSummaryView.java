package com.blastingconcept.lsq.the.ports.persistence.supplier;

public interface SupplierSummaryView {

    String getSupplierId();

    Long getInvoiceTotal();

    Float getInvoiceAmountSum();

    Long getInvoiceIdCountByState();

    String getInvoiceState();
}
