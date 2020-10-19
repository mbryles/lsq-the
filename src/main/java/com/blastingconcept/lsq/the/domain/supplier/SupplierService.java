package com.blastingconcept.lsq.the.domain.supplier;

import java.util.List;

public interface SupplierService {

    List<SupplierSummary> getAllSupplierSummaries();
    List<Supplier> getSupplierById(String id);
}
