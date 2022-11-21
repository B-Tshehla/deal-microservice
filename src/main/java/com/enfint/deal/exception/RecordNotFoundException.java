package com.enfint.deal.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String message){
        super(message);
    }
    public RecordNotFoundException(String massage, Throwable cause){
        super(massage,cause);
    }
}
