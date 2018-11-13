package com.future.office_inventory_system.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ResponseStatus(reason = "FileStorageException")
public class FileStorageException extends RuntimeException {

    public FileStorageException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
