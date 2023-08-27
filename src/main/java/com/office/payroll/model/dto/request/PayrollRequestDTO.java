package com.office.payroll.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@NotNull
public class PayrollRequestDTO {
    @NotBlank
    private String employeeId;
    private Integer daysWorked;
    private Integer daysAbsent;
    private Integer month;
    private Integer year;
}
