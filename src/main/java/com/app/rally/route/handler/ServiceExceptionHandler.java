package com.app.rally.route.handler;

import com.app.rally.route.constant.AppConstant;
import com.app.rally.route.domain.Error;
import com.app.rally.route.domain.ServiceError;
import com.app.rally.route.exception.CluBException;
import com.app.rally.route.exception.FileProcessException;
import com.app.rally.route.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ServiceExceptionHandler {
    @ExceptionHandler(value = UserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ServiceError<List<Error>>> handleServiceException(UserException ex ) {
        log.info("UserException exception occurred {}",ex.getMessage());
        return new ResponseEntity<>(new ServiceError<>(List.of(new Error(ex.getCode(), ex.getMessage()))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CluBException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ServiceError<List<Error>>> handleServiceException(CluBException ex ) {
        log.info("CluBException exception occurred {}",ex.getMessage());
        return new ResponseEntity<>(new ServiceError<>(List.of(new Error(ex.getCode(), ex.getMessage()))), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = FileProcessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ServiceError<List<Error>>> handleServiceException(FileProcessException ex ) {
        log.info("FileProcessException exception occurred {}",ex.getMessage());
        return new ResponseEntity<>(new ServiceError<>(List.of(new Error(ex.getCode(), ex.getMessage()))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ServiceError<List<Error>>> handleServiceException(RuntimeException ex ) {
        log.error("runtime exception occurred ",ex);
        return new ResponseEntity<>(new ServiceError<>(List.of(new Error(AppConstant.RUNTIME_SERVICE_EXCEPTION, ex.getMessage()))), HttpStatus.BAD_REQUEST);
    }



}
