package com.kardidev.poc.cloud.probes.consumer.service.common;

import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.google.common.util.concurrent.UncheckedExecutionException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException() {
        return ResponseEntity.badRequest().body("Wrong API usage");
    }

    @ExceptionHandler({ExecutionException.class, UncheckedExecutionException.class})
    public ResponseEntity handleExecutionException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something bad happened");
    }
}
