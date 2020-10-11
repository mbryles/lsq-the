//package com.blastingconcept.lsq.the.ports.persistence.supplier;
//
//import com.blastingconcept.lsq.the.domain.csv.Supplier;
//import com.wix.mysql.EmbeddedMysql;
//import com.wix.mysql.config.MysqldConfig;
////import org.junit.Before;
////import org.junit.Test;
////import org.junit.runner.RunWith;
//import org.aspectj.lang.annotation.Before;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.TestPropertySource;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import java.util.stream.StreamSupport;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestPropertySource(locations="classpath:application-test.yml")
////@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@DataJpaTest
//public class SupplierRepositoryIntegrationTest {
//
//    @Autowired
//    private SupplierEntityRepository supplierEntityRepository;
//
//    @Test
//    public void testInsert() {
//        SupplierEntityKey key = SupplierEntityKey.builder()
//                .supplierId("ACME")
//                .invoiceId("123")
//                .build();
//
//        SupplierEntity supplierEntity = SupplierEntity.builder()
//                .id(key)
//                .invoiceAmount(42.0f)
//                .invoiceDate(new Date())
//                .invoiceState("FOO")
//                .paymentAmount(32.0f)
//                .terms(90)
//                .paymentDate(new Date())
//                .build();
//
//        this.supplierEntityRepository.save(supplierEntity);
//
//        SupplierEntityKey entityKey = SupplierEntityKey.builder().supplierId("ACME").invoiceId("123").build();
//        Optional<SupplierEntity> foundEntity = this.supplierEntityRepository.findById(entityKey);
//
//        assertTrue(foundEntity.isPresent());
//        assertEquals(foundEntity.orElseGet(SupplierEntity::new), supplierEntity);
//
//    }
//
//    @Test
//    public void testFetchAllRecords() {
//
//        List<SupplierEntity> supplierEntities = StreamSupport.stream(
//                this.supplierEntityRepository.findAll().spliterator(), false)
//                .collect(Collectors.toList());
//
//        assertFalse(supplierEntities.isEmpty());
//
//    }
//}
