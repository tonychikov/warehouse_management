package com.warehousemanagement.controller;

import com.warehousemanagement.ValidationErrorResponse;
import com.warehousemanagement.Violation;
import com.warehousemanagement.exception.BayNotEmptyException;
import com.warehousemanagement.exception.BayNotFoundException;
import com.warehousemanagement.exception.BayPointNumberExceededException;
import com.warehousemanagement.exception.WarehouseNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class WarehouseExceptionHandler {

    @ExceptionHandler(WarehouseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ValidationErrorResponse warehouseNotFoundHandler(WarehouseNotFoundException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("warehouse", ex.getMessage()));
        return error;
    }

    @ExceptionHandler(BayNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ValidationErrorResponse bayNotFoundHandler(BayNotFoundException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("bay", ex.getMessage()));
        return error;
    }

    @ExceptionHandler(BayPointNumberExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse bayPointNumberExceededHandler(BayPointNumberExceededException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("bay", ex.getMessage()));
        return error;
    }

    @ExceptionHandler(BayNotEmptyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse bayNotEmptyHandler(BayNotEmptyException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("occupiedPoints", ex.getMessage()));
        return error;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.getViolations().add(
                    new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getViolations().add(
                    new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return error;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("json", ex.getMessage()));
        return error;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onDataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        error.getViolations().add(new Violation("db", ex.getMessage()));
        return error;
    }
}