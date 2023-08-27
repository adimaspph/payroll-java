package com.office.payroll.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PayrollAlreadyExistException extends RuntimeException {
    public PayrollAlreadyExistException(String message) {
        super(message);
    }
}

