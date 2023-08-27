package com.office.payroll.service.impl;

import com.office.payroll.exception.PayrollAlreadyExistException;
import com.office.payroll.exception.PayrollNotFoundException;
import com.office.payroll.model.Employee;
import com.office.payroll.model.Gender;
import com.office.payroll.model.Payroll;
import com.office.payroll.model.SalaryMatrix;
import com.office.payroll.repository.PayrollRepository;
import com.office.payroll.repository.SalaryMatrixRepository;
import com.office.payroll.service.EmployeeService;
import com.office.payroll.service.PayrollService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@Slf4j
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;

    private final EmployeeService employeeService;

    private final SalaryMatrixRepository salaryMatrixRepository;

    @Autowired
    public PayrollServiceImpl(PayrollRepository payrollRepository, EmployeeService employeeService, SalaryMatrixRepository salaryMatrixRepository) {
        this.payrollRepository = payrollRepository;
        this.employeeService = employeeService;
        this.salaryMatrixRepository = salaryMatrixRepository;
    }

    @Override
    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();
    }

    @Override
    public Payroll getPayrollById(Long id) {
        return payrollRepository.findById(id)
                .orElseThrow(() -> new PayrollNotFoundException("Payroll not found"));
    }

    @Override
    public Payroll createPayroll(Payroll payroll) {
        try {
            return payrollRepository.save(payroll);
        } catch (DataIntegrityViolationException ex) {
            throw new PayrollAlreadyExistException("Payroll with ID " + payroll.getId() + " already exists");
        }
    }

    @Override
    public Payroll createPayrollFromInput(String employeeId, int daysWorked, int daysAbsent, int month, int year) {
        // Retrieve employee by ID
        Employee employee = employeeService.getEmployeeById(employeeId);

        // Calculate the salary based on the provided data
        double basicPayroll = calculateBasicPayroll(employee.getGrade());
        double paycut = calculatePaycut(employee.getGrade(), daysAbsent);
        double additionalSalary = calculateAdditionalSalary(employee, daysWorked);

        if (checkDuplicatePayrollByDate(employeeId, month, year)) {
            throw new PayrollAlreadyExistException(String.format("Payroll with month %d and year %d already exist", month, year));
        }

        // Create Payroll entity and save it
        Payroll payroll = new Payroll();
        payroll.setEmployee(employee);
        payroll.setBasicPayroll(basicPayroll);
        payroll.setPaycut(paycut);
        payroll.setAdditionalSalary(additionalSalary);
        payroll.setMonth(month);
        payroll.setYear(year);
        return payrollRepository.save(payroll);
    }

    private boolean checkDuplicatePayrollByDate(String employeeId, int month, int year) {
        Employee employee = employeeService.getEmployeeById(employeeId);

        for (Payroll payroll : employee.getPayrolls()) {
            if (payroll.getMonth() == month && payroll.getYear() == year) {
                return true;
            }
        }
        return false;
    }
    private double calculateBasicPayroll(Integer grade) {

        SalaryMatrix salaryMatrix = salaryMatrixRepository.findSalaryMatrixByGrade(grade).orElse(null);

        if (salaryMatrix == null) {
            throw new NoSuchElementException("grade is not found");
        }
        return salaryMatrix.getBasicSalary();
    }

    private double calculatePaycut(Integer grade, int daysAbset) {

        SalaryMatrix salaryMatrix = salaryMatrixRepository.findSalaryMatrixByGrade(grade).orElse(null);

        if (salaryMatrix == null) {
            throw new NoSuchElementException("grade is not found");
        }

        return salaryMatrix.getPayCut()*daysAbset;
    }

    private double calculateAdditionalSalary(Employee employee, int daysAbset) {

        SalaryMatrix salaryMatrix = salaryMatrixRepository.findSalaryMatrixByGrade(employee.getGrade()).orElse(null);

        if (salaryMatrix == null) {
            throw new NoSuchElementException("grade is not found");
        }

        double result = salaryMatrix.getAllowance()*daysAbset;

        if (employee.isMarried() && employee.getGender() == Gender.MALE ) {
            result += salaryMatrix.getHeadOfFamily();
        }

        return result;
    }

    @Override
    public Payroll updatePayroll(Long id, Payroll payroll) {
        if (payrollRepository.existsById(id)) {
            payroll.setId(id);
            return payrollRepository.save(payroll);
        } else {
            throw new PayrollNotFoundException("Payroll not found");
        }
    }

    @Override
    public void deletePayroll(Long id) {
        if (payrollRepository.existsById(id)) {
            payrollRepository.deleteById(id);
        } else {
            throw new PayrollNotFoundException("Payroll not found");
        }
    }
}
