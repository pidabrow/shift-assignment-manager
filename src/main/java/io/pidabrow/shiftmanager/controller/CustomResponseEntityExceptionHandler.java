package io.pidabrow.shiftmanager.controller;

import io.pidabrow.shiftmanager.dto.ExceptionResponseDto;
import io.pidabrow.shiftmanager.exception.DomainException;
import io.pidabrow.shiftmanager.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e) {
        ExceptionResponseDto response = new ExceptionResponseDto(String.valueOf(NOT_FOUND.value()), e.getMessage());

        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(DomainException.class)
    public final ResponseEntity<Object> handleDomainException(DomainException e) {
        ExceptionResponseDto response = new ExceptionResponseDto(String.valueOf(BAD_REQUEST.value()), e.getMessage());

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<Object> handleGenericException(Throwable t) {
        ExceptionResponseDto response = new ExceptionResponseDto(String.valueOf(BAD_REQUEST.value()), t.getMessage());

        return new ResponseEntity<>(response, BAD_REQUEST);
    }
}
