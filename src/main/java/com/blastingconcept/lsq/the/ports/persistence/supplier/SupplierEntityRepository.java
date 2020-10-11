package com.blastingconcept.lsq.the.ports.persistence.supplier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierEntityRepository extends CrudRepository<SupplierEntity, SupplierEntityKey> {

    List<SupplierEntity> findBySupplierId(String supplierId);
}
