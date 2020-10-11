package com.blastingconcept.lsq.the.ports.persistence.supplier;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SUPPLIER", schema = "LSQ")
public class SupplierEntity implements Serializable {

    @EmbeddedId
    private SupplierEntityKey id;

    @Column(name = "SUPPLIER_ID", insertable = false, updatable = false)
    private String supplierId;

    @Column(name ="INVOICE_ID", insertable = false, updatable = false)
    private String InvoiceId;

    @Column(name = "INVOICE_DATE")
    private Date invoiceDate;

    @Column(name = "INVOICE_AMOUNT")
    private Float invoiceAmount;

    @Column(name = "TERMS")
    private Integer terms;

    @Column(name = "PAYMENT_DATE")
    private Date paymentDate;

    @Column(name = "PAYMENT_AMOUNT")
    private Float paymentAmount;

    @Column(name = "INVOICE_STATE")
    private String invoiceState;
}
