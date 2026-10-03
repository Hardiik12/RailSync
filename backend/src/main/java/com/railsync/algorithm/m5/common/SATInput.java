package com.railsync.algorithm.m5.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SATInput {
    private List<String> variables;
    private List<List<Literal>> clauses;
    private List<List<String>> stringClauses;
    private Boolean traceEnabled;
    private Integer maxTraceSteps;

    public List<List<Literal>> getNormalizedClauses() {
        if (clauses != null && !clauses.isEmpty()) {
            return clauses;
        }
        if (stringClauses != null && !stringClauses.isEmpty()) {
            List<List<Literal>> normalized = new ArrayList<>();
            for (List<String> sc : stringClauses) {
                if (sc == null) continue;
                List<Literal> c = new ArrayList<>();
                for (String s : sc) {
                    if (s != null) {
                        c.add(Literal.parse(s));
                    }
                }
                normalized.add(c);
            }
            return normalized;
        }
        return new ArrayList<>();
    }
}
