package com.blastingconcept.lsq.the.domain.csv;

import com.blastingconcept.lsq.the.ports.persistence.supplier.SupplierEntityKey;

import java.util.List;

public class DuplicateSupplierKeyException extends RuntimeException {

    private final List<SupplierEntityKey> duplicateRecords;

    public DuplicateSupplierKeyException(String message, List<SupplierEntityKey> duplicateRecords) {
        super(message);
        this.duplicateRecords = duplicateRecords;
    }

    public List<SupplierEntityKey> getDuplicateRecords() {
        return duplicateRecords;
    }
}
