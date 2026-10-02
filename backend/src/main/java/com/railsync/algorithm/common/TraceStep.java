package com.railsync.algorithm.common;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class TraceStep {
    private final int step;
    private final String action;
    private final Map<String, Object> state;
    private final String description;
}
