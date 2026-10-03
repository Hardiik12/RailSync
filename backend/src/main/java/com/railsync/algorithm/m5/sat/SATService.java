package com.railsync.algorithm.m5.sat;

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
public class SATService {

    private final SATAlgorithm satAlgorithm;

    public SATResult execute(SATInput input) {
        if (input == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "SAT input cannot be null");
        }

        List<List<Literal>> clauses = input.getNormalizedClauses();
        List<String> variables = input.getVariables();

        if (clauses == null || clauses.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "SAT formula must contain at least one clause");
        }

        if (variables == null || variables.isEmpty()) {
            // Deduce variables from clauses
            Set<String> varSet = new HashSet<>();
            for (List<Literal> clause : clauses) {
                if (clause == null || clause.isEmpty()) {
                    throw new ApiException(ErrorCode.INVALID_INPUT, "Clauses cannot be null or empty");
                }
                for (Literal lit : clause) {
                    if (lit == null || lit.getVariable() == null || lit.getVariable().trim().isEmpty()) {
                        throw new ApiException(ErrorCode.INVALID_INPUT, "Literal variables cannot be null or empty");
                    }
                    varSet.add(lit.getVariable().trim());
                }
            }
            variables = List.copyOf(varSet);
            input.setVariables(variables);
        }

        if (variables.size() > 20 || clauses.size() > 100) {
            throw new ApiException(ErrorCode.INPUT_TOO_LARGE,
                    "SAT size limit exceeded. Max 20 variables and 100 clauses allowed. Received: "
                            + variables.size() + " variables, " + clauses.size() + " clauses.");
        }

        return satAlgorithm.execute(input);
    }
}
