package com.railsync.common.error;

import com.railsync.common.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException ex) {
        log.error("API Exception [{}]: {}", ex.getErrorCode(), ex.getMessage());
        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(ApiResponse.error(ex.getErrorCode().name(), ex.getMessage(), ex.getDetails()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        log.error("Validation failed: {}", errors);
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(ErrorCode.INVALID_INPUT.name(), "Validation failed", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unhandled exception caught: ", ex);
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.error(ErrorCode.INTERNAL_ERROR.name(), "An unexpected server error occurred", null));
    }
}
