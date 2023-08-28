package com.office.payroll.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SalaryMatrixAlreadyExistException extends RuntimeException {
    public SalaryMatrixAlreadyExistException(String message) {
        super(message);
    }
}

