package com.railsync.algorithm.m5.threesat;

import com.railsync.algorithm.m5.common.Literal;
import com.railsync.algorithm.m5.common.SATInput;
import com.railsync.algorithm.m5.common.SATResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ThreeSATService {

    private final ThreeSATAlgorithm threeSATAlgorithm;

    public SATResult execute(SATInput input) {
        if (input == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "3-SAT input cannot be null");
        }

        List<List<Literal>> clauses = input.getNormalizedClauses();
        if (clauses == null || clauses.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "3-SAT formula must contain at least one clause");
        }

        for (int i = 0; i < clauses.size(); i++) {
            List<Literal> clause = clauses.get(i);
            if (clause == null || clause.size() != 3) {
                throw new ApiException(ErrorCode.INVALID_INPUT,
                        "Each clause in 3-SAT must contain exactly 3 literals. Clause at index " + i + " contains "
                                + (clause != null ? clause.size() : 0) + " literals.");
            }
        }

        List<String> variables = input.getVariables();
        if (variables == null || variables.isEmpty()) {
            Set<String> varSet = new HashSet<>();
            for (List<Literal> clause : clauses) {
                for (Literal lit : clause) {
                    varSet.add(lit.getVariable().trim());
                }
            }
            variables = List.copyOf(varSet);
            input.setVariables(variables);
        }

        if (variables.size() > 20 || clauses.size() > 100) {
            throw new ApiException(ErrorCode.INPUT_TOO_LARGE,
                    "3-SAT size limit exceeded. Max 20 variables and 100 clauses allowed.");
        }

        return threeSATAlgorithm.execute(input);
    }
}
