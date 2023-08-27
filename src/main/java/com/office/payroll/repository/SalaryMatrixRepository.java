package com.office.payroll.repository;

import com.office.payroll.model.SalaryMatrix;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaryMatrixRepository extends JpaRepository<SalaryMatrix, Long> {
    Optional<SalaryMatrix> findSalaryMatrixByGrade(Integer grade);

    Boolean existsByGrade(Integer grade);




}

