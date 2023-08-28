package com.office.payroll.controller;

import com.office.payroll.model.Employee;
import com.office.payroll.model.SalaryMatrix;
import com.office.payroll.model.dto.response.PayrollResponse;
import com.office.payroll.model.dto.response.ResponseTemplate;
import com.office.payroll.service.EmployeeService;
import com.office.payroll.service.SalaryMatrixService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final SalaryMatrixService salaryMatrixService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, SalaryMatrixService salaryMatrixService) {
        this.employeeService = employeeService;
        this.salaryMatrixService = salaryMatrixService;
    }

    @GetMapping
    public ResponseEntity<ResponseTemplate<List<Employee>>> getAllEmployees() {
        ResponseTemplate<List<Employee>> responseTemplate = new ResponseTemplate<>();
        responseTemplate.statusOk(employeeService.getAllEmployees());
        return ResponseEntity.ok(responseTemplate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseTemplate<Employee>> getEmployeeById(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        ResponseTemplate<Employee> responseTemplate = new ResponseTemplate<>();
        responseTemplate.statusOk(employee);
        return ResponseEntity.ok(responseTemplate);
    }

    @GetMapping("/{id}/payroll")
    public ResponseEntity<ResponseTemplate<PayrollResponse>> getAllPayrollEmployeeById(@PathVariable String id) {
        Employee employee = employeeService.getEmployeeById(id);
        SalaryMatrix salaryMatrix = salaryMatrixService.getSalaryMatrixByGrade(employee.getGrade());

        PayrollResponse payrollResponse = new PayrollResponse(employee, salaryMatrix);

        ResponseTemplate<PayrollResponse> responseTemplate = new ResponseTemplate<>();
        responseTemplate.statusOk(payrollResponse);

        return ResponseEntity.ok(responseTemplate);
    }

    @PostMapping
    public ResponseEntity<ResponseTemplate<Employee>> createEmployee(@Valid @RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);

        ResponseTemplate<Employee> responseTemplate = new ResponseTemplate<>();
        responseTemplate.setTimestamp(LocalDateTime.now());
        responseTemplate.setStatus(HttpStatus.CREATED.toString());
        responseTemplate.setData(createdEmployee);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseTemplate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseTemplate<Employee>> updateEmployee(@PathVariable String id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        ResponseTemplate<Employee> responseTemplate = new ResponseTemplate<>();
        responseTemplate.statusOk(updatedEmployee);
        return ResponseEntity.ok(responseTemplate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseTemplate<String>> deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployee(id);
        ResponseTemplate<String> responseTemplate = new ResponseTemplate<>();
        responseTemplate.statusOk("deleted");
        return ResponseEntity.ok(responseTemplate);
    }
}

