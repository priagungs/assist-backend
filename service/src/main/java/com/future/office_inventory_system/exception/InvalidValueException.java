package com.future.office_inventory_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidValueException extends RuntimeException {

    public InvalidValueException(String exception) {
        super(exception);
    }

}
