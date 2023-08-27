package com.office.payroll.controller;

import com.office.payroll.model.Employee;
import com.office.payroll.model.Payroll;
import com.office.payroll.model.dto.request.PayrollRequestDTO;
import com.office.payroll.model.dto.response.ResponseTemplate;
import com.office.payroll.service.PayrollService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payrolls")
public class PayrollController {

    private final PayrollService payrollService;

    @Autowired
    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @GetMapping
    public ResponseEntity<ResponseTemplate<List<Payroll>>> getAllPayrolls() {
        ResponseTemplate<List<Payroll>> responseTemplate = new ResponseTemplate<>();
        responseTemplate.statusOk(payrollService.getAllPayrolls());

        return ResponseEntity.ok(responseTemplate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseTemplate<Payroll>> getPayrollById(@PathVariable Long id) {
        Payroll payroll = payrollService.getPayrollById(id);

        ResponseTemplate<Payroll> responseTemplate = new ResponseTemplate<>();
        responseTemplate.statusOk(payroll);

        return ResponseEntity.ok(responseTemplate);
    }

    @PostMapping
    public ResponseEntity<ResponseTemplate<Payroll>> createPayroll(@Valid @RequestBody PayrollRequestDTO payrollRequestDTO) {
        Payroll createdPayroll = payrollService.createPayrollFromInput(
                payrollRequestDTO.getEmployeeId(),
                payrollRequestDTO.getDaysWorked(),
                payrollRequestDTO.getDaysAbsent(),
                payrollRequestDTO.getMonth(),
                payrollRequestDTO.getYear()
        );

        ResponseTemplate<Payroll> responseTemplate = new ResponseTemplate<>();
        responseTemplate.statusOk(createdPayroll);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseTemplate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseTemplate<Payroll>> updatePayroll(@PathVariable Long id, @RequestBody Payroll payroll) {
        Payroll updatedPayroll = payrollService.updatePayroll(id, payroll);

        ResponseTemplate<Payroll> responseTemplate = new ResponseTemplate<>();
        responseTemplate.statusOk(updatedPayroll);

        return ResponseEntity.ok(responseTemplate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseTemplate<String>> deletePayroll(@PathVariable Long id) {
        payrollService.deletePayroll(id);

        ResponseTemplate<String> responseTemplate = new ResponseTemplate<>();
        responseTemplate.statusOk("Payroll with id " + id + " has been deleted");

        return ResponseEntity.ok(responseTemplate);
    }
}
