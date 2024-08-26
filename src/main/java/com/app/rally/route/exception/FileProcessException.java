package com.app.rally.route.exception;

import lombok.Getter;

public class FileProcessException extends RuntimeException {
    @Getter
    private final String code;
    public FileProcessException(String msg, String errorCode) {
        super(msg);
        this.code=errorCode;

    }


}
