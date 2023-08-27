package com.office.payroll.service;

import com.office.payroll.model.SalaryMatrix;

import java.util.List;

public interface SalaryMatrixService {
    List<SalaryMatrix> getAllSalaryMatrices();
    SalaryMatrix getSalaryMatrixById(Long id);

    Boolean checkIfGradeExist(int grade);
    SalaryMatrix createSalaryMatrix(SalaryMatrix salaryMatrix);
    SalaryMatrix updateSalaryMatrix(Long id, SalaryMatrix salaryMatrix);
    void deleteSalaryMatrix(Long id);
    SalaryMatrix getSalaryMatrixByGrade(int grade);
}

