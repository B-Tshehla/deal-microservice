package com.enfint.deal.exception.recordNotFound;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class RecordNotFound {
    private String message;
    private HttpStatus httpStatus;
    private ZonedDateTime timestamp;


}
