package com.blastingconcept.lsq.the.domain.supplier;

import lombok.*;

import java.util.Date;

@Data
@ToString
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

    private String supplierId;

    private String invoiceId;

    private Date invoiceDate;

    private Float invoiceAmount;

    private Integer terms;

    private Date paymentDate;

    private Float paymentAmount;
}
