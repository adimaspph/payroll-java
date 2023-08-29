package com.office.payroll.service.impl;

import com.office.payroll.exception.custom.EmployeeAlreadyExistException;
import com.office.payroll.exception.custom.EmployeeNotFoundException;
import com.office.payroll.model.Employee;
import com.office.payroll.repository.EmployeeRepository;
import com.office.payroll.service.EmployeeService;
import com.office.payroll.service.SalaryMatrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final SalaryMatrixService salaryMatrixService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, SalaryMatrixService salaryMatrixService) {
        this.employeeRepository = employeeRepository;
        this.salaryMatrixService = salaryMatrixService;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> getAllEmployeesPage(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return employeeRepository.findAll(pageable).stream().toList();
    }

    @Override
    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
    }

    @Override
    public Employee createEmployee(Employee employee) {
        try {
            if (!salaryMatrixService.checkIfGradeExist(employee.getGrade())) {
                throw new IllegalArgumentException("Grade " + employee.getGrade() + " is not exist");
            }
            return employeeRepository.save(employee);
        } catch (DataIntegrityViolationException ex) {
            throw new EmployeeAlreadyExistException("Employee with ID " + employee.getId() + " already exists");
        }
    }

    @Override
    public Employee updateEmployee(String id, Employee employee) {
        if (employeeRepository.existsById(id)) {
            employee.setId(id);
            return employeeRepository.save(employee);
        } else {
            throw new EmployeeNotFoundException("Employee not found");
        }
    }

    @Override
    public void deleteEmployee(String id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        } else {
            throw new EmployeeNotFoundException("Employee not found");
        }
    }
}


