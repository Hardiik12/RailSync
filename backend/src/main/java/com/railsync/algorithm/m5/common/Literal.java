package com.railsync.algorithm.m5.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Literal {
    private String variable;
    private boolean positive;

    public static Literal parse(String str) {
        if (str == null || str.trim().isEmpty()) {
            throw new IllegalArgumentException("Literal string cannot be null or empty");
        }
        String trimmed = str.trim();
        if (trimmed.startsWith("!")) {
            return new Literal(trimmed.substring(1).trim(), false);
        } else if (trimmed.startsWith("~")) {
            return new Literal(trimmed.substring(1).trim(), false);
        } else if (trimmed.startsWith("NOT ")) {
            return new Literal(trimmed.substring(4).trim(), false);
        } else {
            return new Literal(trimmed, true);
        }
    }

    public String toFormulaString() {
        return positive ? variable : "!" + variable;
    }

    public Literal negate() {
        return new Literal(variable, !positive);
    }
}
