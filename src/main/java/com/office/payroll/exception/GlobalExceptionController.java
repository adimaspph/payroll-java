package com.office.payroll.exception;

import com.office.payroll.exception.custom.*;
import com.office.payroll.model.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);

    @ExceptionHandler(value = {
            EmployeeNotFoundException.class,
            PayrollNotFoundException.class,
            SalaryMatrixNotFoundException.class
    })
    protected ResponseEntity<Object> handleNotFoundExceptions(final RuntimeException ex, final WebRequest request) {
        logger.error("handle NotFoundException", ex);
        String errorMessage = ex.getMessage();
        ErrorResponse errorResponseTemplate = new ErrorResponse(HttpStatus.NOT_FOUND.value(), errorMessage);
        return handleExceptionInternal(ex, errorResponseTemplate, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {
            SalaryMatrixAlreadyExistException.class,
            PayrollAlreadyExistException.class,
            EmployeeAlreadyExistException.class
    })
    protected ResponseEntity<Object> handleAlreadyExistExceptions(final RuntimeException ex, final WebRequest request) {
        logger.error("handle AlreadyExistException", ex);
        String errorMessage = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), errorMessage);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.error("handle MethodArgumentNotValidException", ex);
        Map<String, String> errors = new HashMap<>();
        for (ObjectError error:  ex.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors.toString());
        return createResponseEntity(errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    // Default exception handler for unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllOtherExceptions(final Exception ex, final WebRequest request) {
        logger.error("handle All Other Exceptions", ex);
        ErrorResponse errorResponseTemplate = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return handleExceptionInternal(ex, errorResponseTemplate, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
