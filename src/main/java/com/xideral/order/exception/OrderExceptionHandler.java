package com.xideral.order.exception;

import com.xideral.order.dto.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class OrderExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final var error = String.join(",", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GenericResponse.builder().error(errors).data(null).code("400").message("Error").build());
    }

    @ExceptionHandler(OrderNoFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public final ResponseEntity<GenericResponse> orderNotFoudExceptionHandle(OrderNoFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(GenericResponse.builder().error(Arrays.asList(ex.getMessage())).data(null).code("404").message("Error").build());
    }

    @ExceptionHandler(UserNoFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public final ResponseEntity<GenericResponse> userNotFoudExceptionHandle(UserNoFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(GenericResponse.builder().error(Arrays.asList(ex.getMessage())).data(null).code("404").message("Error").build());
    }

    @ExceptionHandler(NoFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public final ResponseEntity<GenericResponse> notFoudExceptionHandle(NoFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(GenericResponse.builder().error(Arrays.asList(ex.getMessage())).data(null).code("404").message("Error").build());
    }

    @ExceptionHandler(UserServiceUnavailable.class)
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<GenericResponse> handleUserServiceUnavailable(UserServiceUnavailable ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(GenericResponse.builder().error(Arrays.asList(ex.getMessage())).data(null).code("503").message("Error").build());
    }

}
