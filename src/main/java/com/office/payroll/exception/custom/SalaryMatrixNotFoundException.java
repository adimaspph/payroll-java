package com.office.payroll.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SalaryMatrixNotFoundException extends RuntimeException {
    public SalaryMatrixNotFoundException(String message) {
        super(message);
    }
}

