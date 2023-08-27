package com.office.payroll.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.office.payroll.model.Payroll;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PayrollDetailResponse {
    double totalPaycut;
    double totalAllowance;
    double headOfFamily;
    double basicSalary;
    double totalSalary;
    int month;
    int year;

    public PayrollDetailResponse(Payroll payroll, double headOfFamily) {
        this.totalPaycut = payroll.getPaycut();
        this.totalAllowance = payroll.getAdditionalSalary();
        this.headOfFamily = headOfFamily;
        this.basicSalary = payroll.getBasicPayroll();
        this.totalSalary = this.basicSalary + this.totalAllowance - this.totalPaycut;
        this.month = payroll.getMonth();
        this.year = payroll.getYear();
    }
}
