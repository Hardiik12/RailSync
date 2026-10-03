package com.railsync.algorithm.m5.reductions.threesat_clique;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m5.common.CliqueReductionResult;
import com.railsync.algorithm.m5.common.Literal;
import com.railsync.algorithm.m5.common.SATInput;
import com.railsync.algorithm.m5.common.SATResult;
import com.railsync.algorithm.m5.sat.SATAlgorithm;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ThreeSATToCliqueReduction implements Algorithm<SATInput, CliqueReductionResult> {

    private final SATAlgorithm satAlgorithm;

    @Override
    public String getName() {
        return "3SAT_TO_CLIQUE";
    }

    @Override
    public Complexity getComplexity() {
        return new Complexity("O(m^2)", "O(m^2)");
    }

    @Override
    public CliqueReductionResult execute(SATInput input) {
        long startTime = System.nanoTime();
        long opCount = 0;

        List<List<Literal>> clauses = input.getNormalizedClauses();
        if (clauses == null || clauses.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "3-SAT formula must contain at least one clause");
        }

        int m = clauses.size();
        for (int i = 0; i < m; i++) {
            List<Literal> clause = clauses.get(i);
            if (clause == null || clause.size() != 3) {
                throw new ApiException(ErrorCode.INVALID_INPUT,
                        "Each clause in 3-SAT to CLIQUE reduction must contain exactly 3 literals. Clause at index " + i
                                + " has size " + (clause != null ? clause.size() : 0));
            }
        }

        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 50;
        List<TraceStep> trace = new ArrayList<>();

        List<String> vertices = new ArrayList<>();
        Map<String, String> mapping = new LinkedHashMap<>();
        List<VertexMeta> vertexMetaList = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            List<Literal> clause = clauses.get(i);
            for (int j = 0; j < 3; j++) {
                Literal lit = clause.get(j);
                String vId = "C" + (i + 1) + "_L" + (j + 1) + ":" + lit.toFormulaString();
                vertices.add(vId);
                mapping.put(vId, "Clause " + (i + 1) + ", Literal " + (j + 1) + ": " + lit.toFormulaString());
                vertexMetaList.add(new VertexMeta(vId, i, lit));
            }
        }

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(trace.size() + 1)
                    .action("CREATE_VERTICES")
                    .state(Map.of("vertexCount", vertices.size(), "targetCliqueSize", m))
                    .description("Created " + vertices.size() + " vertices for " + m + " clauses")
                    .build());
        }

        List<List<String>> edges = new ArrayList<>();
        for (int a = 0; a < vertexMetaList.size(); a++) {
            VertexMeta u = vertexMetaList.get(a);
            for (int b = a + 1; b < vertexMetaList.size(); b++) {
                VertexMeta v = vertexMetaList.get(b);
                opCount++;

                // Connect if different clauses AND compatible literals
                if (u.clauseIndex != v.clauseIndex) {
                    if (areCompatible(u.literal, v.literal)) {
                        edges.add(List.of(u.id, v.id));
                    }
                }
            }
        }

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(trace.size() + 1)
                    .action("CONNECT_EDGES")
                    .state(Map.of("edgeCount", edges.size()))
                    .description("Created " + edges.size() + " compatibility edges between different clause literals")
                    .build());
        }

        SATResult satResult = satAlgorithm.execute(input);
        boolean satisfiable = satResult.isSatisfiable();
        List<String> cliqueFound = new ArrayList<>();

        if (satisfiable && satResult.getAssignment() != null) {
            Map<String, Boolean> assign = satResult.getAssignment();
            for (int i = 0; i < m; i++) {
                List<Literal> clause = clauses.get(i);
                for (int j = 0; j < 3; j++) {
                    Literal lit = clause.get(j);
                    Boolean val = assign.get(lit.getVariable());
                    boolean isLitTrue = (val != null && val && lit.isPositive()) || (val != null && !val && !lit.isPositive());
                    if (isLitTrue) {
                        String vId = "C" + (i + 1) + "_L" + (j + 1) + ":" + lit.toFormulaString();
                        cliqueFound.add(vId);
                        break;
                    }
                }
            }
        }

        long endTime = System.nanoTime();

        return CliqueReductionResult.builder()
                .sourceProblem("3-SAT")
                .targetProblem("CLIQUE")
                .targetCliqueSize(m)
                .vertices(vertices)
                .edges(edges)
                .mapping(mapping)
                .satisfiable(satisfiable)
                .cliqueFound(cliqueFound)
                .trace(trace)
                .executionTimeNanos(endTime - startTime)
                .operationCount(opCount)
                .complexity(getComplexity())
                .build();
    }

    private boolean areCompatible(Literal l1, Literal l2) {
        if (l1.getVariable().equals(l2.getVariable())) {
            return l1.isPositive() == l2.isPositive();
        }
        return true;
    }

    private record VertexMeta(String id, int clauseIndex, Literal literal) {}
}
