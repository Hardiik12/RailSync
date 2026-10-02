package com.railsync.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorDetails error;
    private final Map<String, Object> meta;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .meta(Map.of(
                        "requestId", UUID.randomUUID().toString(),
                        "timestamp", Instant.now().toString()
                ))
                .build();
    }

    public static <T> ApiResponse<T> success(T data, Map<String, Object> additionalMeta) {
        Map<String, Object> metaMap = new java.util.HashMap<>();
        metaMap.put("requestId", UUID.randomUUID().toString());
        metaMap.put("timestamp", Instant.now().toString());
        if (additionalMeta != null) {
            metaMap.putAll(additionalMeta);
        }

        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .meta(metaMap)
                .build();
    }

    public static <T> ApiResponse<T> error(String code, String message, Object details) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(new ErrorDetails(code, message, details))
                .meta(Map.of(
                        "requestId", UUID.randomUUID().toString(),
                        "timestamp", Instant.now().toString()
                ))
                .build();
    }

    @Getter
    public static class ErrorDetails {
        private final String code;
        private final String message;
        private final Object details;

        public ErrorDetails(String code, String message, Object details) {
            this.code = code;
            this.message = message;
            this.details = details;
        }
    }
}
