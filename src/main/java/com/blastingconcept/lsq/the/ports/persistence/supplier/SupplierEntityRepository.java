package com.blastingconcept.lsq.the.ports.persistence.supplier;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierEntityRepository extends CrudRepository<SupplierEntity, SupplierEntityKey> {

    List<SupplierEntity> findBySupplierId(String supplierId);

    @Query(name = "findAllSupplierSummaries", nativeQuery = true,
        value = "select\n" +
                "    SUPPLIER.supplier_id as supplierId, count(INVOICE_ID) as invoiceTotal, INV_AMT_SUM as invoiceAmountSum, INVOICE_ID_CNT as invoiceIdCountByState, SUPPLIER_SUM.INVOICE_STATE as invoiceState \n" +
                "from\n" +
                "\n" +
                "     supplier\n" +
                "\n" +
                "inner join (select\n" +
                "                SUPPLIER.supplier_id, count(INVOICE_ID) as INVOICE_ID_CNT, SUPPLIER.INVOICE_STATE, (sum(INVOICE_AMOUNT) - sum(PAYMENT_AMOUNT)) as INV_AMT_SUM\n" +
                "            from\n" +
                "                SUPPLIER\n" +
                "            where\n" +
                "                    INVOICE_STATE = 'LATE' OR INVOICE_STATE = 'OPEN'\n" +
                "            group by\n" +
                "                SUPPLIER_ID, INVOICE_STATE) as SUPPLIER_SUM\n" +
                "on\n" +
                "    SUPPLIER.SUPPLIER_ID = SUPPLIER_SUM.SUPPLIER_ID\n" +
                "group by\n" +
                "    SUPPLIER.SUPPLIER_ID, SUPPLIER_SUM.INV_AMT_SUM, SUPPLIER_SUM.INVOICE_ID_CNT, SUPPLIER_SUM.INVOICE_STATE"
    )
    List<SupplierSummaryView> findAllSupplierSummaries();
}
