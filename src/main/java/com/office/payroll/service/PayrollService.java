package com.office.payroll.service;

import com.office.payroll.model.Payroll;

import java.util.List;

public interface PayrollService {
    List<Payroll> getAllPayrolls();
    Payroll getPayrollById(Long id);
    Payroll createPayroll(Payroll payroll);
    Payroll createPayrollFromInput(String employeeId, int daysWorked, int daysAbsent, int month, int year);
    Payroll updatePayroll(Long id, Payroll payroll);
    void deletePayroll(Long id);
}


