package com.future.office_inventory_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)

public class FileStorageException extends RuntimeException {

    public FileStorageException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
