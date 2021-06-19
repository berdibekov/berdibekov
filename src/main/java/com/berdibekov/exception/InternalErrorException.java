package com.berdibekov.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InternalErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InternalErrorException() {
        super();
    }

    public InternalErrorException(String message) {
        super(message);
    }
}
