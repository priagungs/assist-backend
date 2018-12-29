package com.future.assist.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)

public class FileStorageException extends RuntimeException {

    public FileStorageException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
