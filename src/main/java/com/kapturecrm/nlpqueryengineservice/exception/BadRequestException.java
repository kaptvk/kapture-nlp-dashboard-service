package com.kapturecrm.nlpqueryengineservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException() {
        super();
    }
}