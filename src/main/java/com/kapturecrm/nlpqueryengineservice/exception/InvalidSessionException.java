package com.kapturecrm.nlpqueryengineservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidSessionException extends Exception {
    private String message;
    public InvalidSessionException() {
        super();
    }

    public InvalidSessionException(String message) {
        super(message);
        this.message = message;
    }
}