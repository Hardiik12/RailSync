package com.railsync.algorithm.m5.reductions.threesat_clique;

import com.railsync.algorithm.m5.common.CliqueReductionResult;
import com.railsync.algorithm.m5.common.Literal;
import com.railsync.algorithm.m5.common.SATInput;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThreeSATToCliqueService {

    private final ThreeSATToCliqueReduction reduction;

    public CliqueReductionResult execute(SATInput input) {
        if (input == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Input cannot be null");
        }

        List<List<Literal>> clauses = input.getNormalizedClauses();
        if (clauses == null || clauses.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "3-SAT formula must contain at least one clause");
        }

        for (int i = 0; i < clauses.size(); i++) {
            List<Literal> clause = clauses.get(i);
            if (clause == null || clause.size() != 3) {
                throw new ApiException(ErrorCode.INVALID_INPUT,
                        "Each clause in 3-SAT to CLIQUE reduction must contain exactly 3 literals. Clause at index " + i
                                + " contains " + (clause != null ? clause.size() : 0) + " literals.");
            }
        }

        if (clauses.size() > 50) {
            throw new ApiException(ErrorCode.INPUT_TOO_LARGE,
                    "3-SAT to CLIQUE reduction limit exceeded. Max 50 clauses allowed.");
        }

        return reduction.execute(input);
    }
}
