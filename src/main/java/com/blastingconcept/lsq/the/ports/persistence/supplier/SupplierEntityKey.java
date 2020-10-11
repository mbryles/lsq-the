package com.blastingconcept.lsq.the.ports.persistence.supplier;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@ToString
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SupplierEntityKey implements Serializable {

    @Column(name = "SUPPLIER_ID")
    private String supplierId;

    @Column(name = "INVOICE_ID")
    private String invoiceId;
}
