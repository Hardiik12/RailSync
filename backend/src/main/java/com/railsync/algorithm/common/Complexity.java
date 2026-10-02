package com.railsync.algorithm.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Complexity {
    private final String time;
    private final String space;
}
