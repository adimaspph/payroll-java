package com.office.payroll.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.office.payroll.model.Employee;
import com.office.payroll.model.Gender;
import com.office.payroll.model.Payroll;
import com.office.payroll.model.SalaryMatrix;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PayrollResponse {
    Employee employee;
    List<PayrollDetailResponse> payrollDetailResponses = new ArrayList<>();

    public PayrollResponse(Employee employee, SalaryMatrix salaryMatrix) {
        this.employee = employee;
        double headOfFamily = 0;

        if (employee.isMarried() && employee.getGender() == Gender.MALE) {
            headOfFamily = salaryMatrix.getHeadOfFamily();
        }

        for (Payroll payroll : employee.getPayrolls()) {
            this.payrollDetailResponses.add(new PayrollDetailResponse(payroll, headOfFamily));
        }
    }
}
