package com.railsync.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "Invalid request payload or parameters"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Requested resource not found"),
    ALGORITHM_ERROR(HttpStatus.UNPROCESSABLE_ENTITY, "Algorithm execution failed"),
    INPUT_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "Input size exceeds configured threshold"),
    TRACE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "Requested trace steps exceed configured maximum limit"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred");

    private final HttpStatus httpStatus;
    private final String defaultMessage;

    ErrorCode(HttpStatus httpStatus, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }
}
