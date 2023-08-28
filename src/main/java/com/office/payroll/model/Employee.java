package com.office.payroll.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @NotBlank(message = "name is mandatory")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "gender is mandatory")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    private Integer grade;

    @Column(nullable = false)
    @NotNull(message = "IsMarried is mandatory")
    private boolean isMarried;

    @OneToMany(mappedBy="employee")
    @JsonIgnore
    private List<Payroll> payrolls;
}
