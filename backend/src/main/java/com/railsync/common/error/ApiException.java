package com.railsync.common.error;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object details;

    public ApiException(ErrorCode errorCode, String message) {
        super(message != null ? message : errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    public ApiException(ErrorCode errorCode, String message, Object details) {
        super(message != null ? message : errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.details = details;
    }
}
