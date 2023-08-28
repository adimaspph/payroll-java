package com.office.payroll.repository;

import com.office.payroll.model.SalaryMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalaryMatrixRepository extends JpaRepository<SalaryMatrix, Long> {
    Optional<SalaryMatrix> findSalaryMatrixByGrade(Integer grade);

    Boolean existsByGrade(Integer grade);




}

