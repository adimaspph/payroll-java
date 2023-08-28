package com.office.payroll.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NotNull
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PayrollRequestDTO {
    @NotNull
    private String employeeId;

    @Min(0)
    private Integer daysWorked;

    @Min(0)
    private Integer daysAbsent;

    @Min(0)
    private Integer month;

    @Min(0)
    private Integer year;
}
