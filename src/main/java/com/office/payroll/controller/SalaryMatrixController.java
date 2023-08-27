package com.office.payroll.controller;

import com.office.payroll.model.SalaryMatrix;
import com.office.payroll.model.dto.response.ResponseTemplate;
import com.office.payroll.service.SalaryMatrixService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salary-matrices")
public class SalaryMatrixController {

    private final SalaryMatrixService salaryMatrixService;

    @Autowired
    public SalaryMatrixController(SalaryMatrixService salaryMatrixService) {
        this.salaryMatrixService = salaryMatrixService;
    }

    @GetMapping
    public ResponseEntity<ResponseTemplate<List<SalaryMatrix>>> getAllSalaryMatrices() {
        ResponseTemplate<List<SalaryMatrix>> responseTemplate = new ResponseTemplate<>();
        responseTemplate.statusOk(salaryMatrixService.getAllSalaryMatrices());
        return ResponseEntity.ok(responseTemplate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseTemplate<SalaryMatrix>> getSalaryMatrixById(@PathVariable Long id) {
        SalaryMatrix salaryMatrix = salaryMatrixService.getSalaryMatrixById(id);

        ResponseTemplate<SalaryMatrix> responseTemplate = new ResponseTemplate<>();
        responseTemplate.statusOk(salaryMatrix);
        return ResponseEntity.ok(responseTemplate);
    }

    @PostMapping
    public ResponseEntity<ResponseTemplate<SalaryMatrix>> createSalaryMatrix(@Valid @RequestBody SalaryMatrix salaryMatrix) {
        SalaryMatrix createdSalaryMatrix = salaryMatrixService.createSalaryMatrix(salaryMatrix);
        ResponseTemplate<SalaryMatrix> responseTemplate = new ResponseTemplate<>();
        responseTemplate.setStatus(HttpStatus.OK.toString());
        responseTemplate.setData(createdSalaryMatrix);
        return ResponseEntity.ok(responseTemplate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseTemplate<SalaryMatrix>> updateSalaryMatrix(@PathVariable Long id, @RequestBody SalaryMatrix salaryMatrix) {
        SalaryMatrix updatedSalaryMatrix = salaryMatrixService.updateSalaryMatrix(id, salaryMatrix);

        ResponseTemplate<SalaryMatrix> responseTemplate = new ResponseTemplate<>();
        responseTemplate.statusOk(updatedSalaryMatrix);

        return ResponseEntity.ok(responseTemplate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalaryMatrix(@PathVariable Long id) {
        salaryMatrixService.deleteSalaryMatrix(id);
        return ResponseEntity.noContent().build();
    }
}

