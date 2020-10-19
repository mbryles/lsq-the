package com.blastingconcept.lsq.the.ports.rest.supplier;

import com.blastingconcept.lsq.the.domain.supplier.Supplier;
import com.blastingconcept.lsq.the.domain.supplier.SupplierService;
import com.blastingconcept.lsq.the.domain.supplier.SupplierSummary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Controller
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping(value = "{id}", produces = "application/json")
    public @ResponseBody List<Supplier> getSupplierRecordsById(@PathVariable("id") String id) {

        List<Supplier> suppliers = this.supplierService.getSupplierById(id);

        return suppliers;

    }

    @GetMapping(value = "/summary", produces = "application/json")
    public ResponseEntity getSupplierSummaries() {

        List<SupplierSummary> views = this.supplierService.getAllSupplierSummaries();

        return ResponseEntity.status(HttpStatus.OK).body(views);

    }
}
