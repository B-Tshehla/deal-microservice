package com.enfint.deal.exception.recordNotFound;


import com.enfint.deal.exception.applicationDenied.ApplicationDeniedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RecordNotFoundExceptionHandler {

    @ExceptionHandler(value = {ApplicationDeniedException.class})
    public ResponseEntity<Object> handleRecordNotFoundException(RecordNotFoundException e){
        RecordNotFound recordNotFound = new RecordNotFound(
                e.getMessage(),
                NOT_FOUND,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(recordNotFound, NOT_FOUND);
    }
}
