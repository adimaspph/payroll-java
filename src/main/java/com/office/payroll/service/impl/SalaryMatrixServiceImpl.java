package com.office.payroll.service.impl;

import com.office.payroll.exception.custom.SalaryMatrixAlreadyExistException;
import com.office.payroll.exception.custom.SalaryMatrixNotFoundException;
import com.office.payroll.model.SalaryMatrix;
import com.office.payroll.repository.SalaryMatrixRepository;
import com.office.payroll.service.SalaryMatrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@Slf4j
public class SalaryMatrixServiceImpl implements SalaryMatrixService {

    private final SalaryMatrixRepository salaryMatrixRepository;

    @Autowired
    public SalaryMatrixServiceImpl(SalaryMatrixRepository salaryMatrixRepository) {
        this.salaryMatrixRepository = salaryMatrixRepository;
    }

    @Override
    public List<SalaryMatrix> getAllSalaryMatrices() {
        return salaryMatrixRepository.findAll();
    }

    @Override
    public SalaryMatrix getSalaryMatrixById(Long id) {
        return salaryMatrixRepository.findById(id)
                .orElseThrow(() -> new SalaryMatrixNotFoundException("Salary Matrix not found"));
    }

    @Override
    public Boolean checkIfGradeExist(int grade) {
        return salaryMatrixRepository.existsByGrade(grade);
    }

    @Override
    public SalaryMatrix getSalaryMatrixByGrade(int grade) {
        SalaryMatrix salaryMatrix = salaryMatrixRepository.findSalaryMatrixByGrade(grade).orElse(null);

        if (salaryMatrix == null) {
            throw new SalaryMatrixNotFoundException("Salary Matrix not found");
        }

        return salaryMatrix;
    }

    @Override
    public SalaryMatrix createSalaryMatrix(SalaryMatrix salaryMatrix) {
        try {
            return salaryMatrixRepository.save(salaryMatrix);
        } catch (DataIntegrityViolationException ex) {
            throw new SalaryMatrixAlreadyExistException("Salary Matrix with ID " + salaryMatrix.getId() + " already exists");
        }
    }

    @Override
    public SalaryMatrix updateSalaryMatrix(Long id, SalaryMatrix salaryMatrix) {
        if (salaryMatrixRepository.existsById(id)) {
            salaryMatrix.setId(id);
            return salaryMatrixRepository.save(salaryMatrix);
        } else {
            throw new SalaryMatrixNotFoundException("Salary Matrix not found");
        }
    }

    @Override
    public void deleteSalaryMatrix(Long id) {
        if (salaryMatrixRepository.existsById(id)) {
            salaryMatrixRepository.deleteById(id);
        } else {
            throw new SalaryMatrixNotFoundException("Salary Matrix not found");
        }
    }
}


