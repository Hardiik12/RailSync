package com.railsync.algorithm.m5.threesat;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m5.common.Literal;
import com.railsync.algorithm.m5.common.SATInput;
import com.railsync.algorithm.m5.common.SATResult;
import com.railsync.algorithm.m5.sat.SATAlgorithm;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ThreeSATAlgorithm implements Algorithm<SATInput, SATResult> {

    private final SATAlgorithm satAlgorithm;

    @Override
    public String getName() {
        return "3-SAT";
    }

    @Override
    public Complexity getComplexity() {
        return new Complexity("O(2^V)", "O(V + C)");
    }

    @Override
    public SATResult execute(SATInput input) {
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

        SATResult result = satAlgorithm.execute(input);
        result.setAlgorithm(getName());
        result.setComplexity(getComplexity());
        return result;
    }
}
