package com.enfint.deal.exception.applicationDenied;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;


@ControllerAdvice
public class ApplicationDeniedExceptionHandler {

    @ExceptionHandler(value = {ApplicationDeniedException.class})
    public ResponseEntity<Object> handleApplicationDeniedException(ApplicationDeniedException e){
        ApplicationDenied applicationDenied = new ApplicationDenied(
                e.getMessage(),
                NOT_ACCEPTABLE,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(applicationDenied, NOT_ACCEPTABLE);
    }

}
