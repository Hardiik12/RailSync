package com.railsync.algorithm.m5.reductions.clique_is;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m5.common.CliqueToISInput;
import com.railsync.algorithm.m5.common.CliqueToISResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CliqueToIndependentSetReduction implements Algorithm<CliqueToISInput, CliqueToISResult> {

    @Override
    public String getName() {
        return "CLIQUE_TO_INDEPENDENT_SET";
    }

    @Override
    public Complexity getComplexity() {
        return new Complexity("O(V^2)", "O(V + E)");
    }

    @Override
    public CliqueToISResult execute(CliqueToISInput input) {
        long startTime = System.nanoTime();
        long opCount = 0;

        List<String> vertices = input.getVertices() != null ? input.getVertices() : new ArrayList<>();
        List<List<String>> edges = input.getEdges() != null ? input.getEdges() : new ArrayList<>();
        int k = input.getCliqueSize() != null ? input.getCliqueSize() : 0;

        if (vertices.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Vertices list cannot be empty");
        }

        Set<String> vertexSet = new HashSet<>(vertices);
        Set<String> edgeSet = new HashSet<>();

        for (List<String> edge : edges) {
            if (edge == null || edge.size() != 2) {
                throw new ApiException(ErrorCode.INVALID_INPUT, "Each edge must contain exactly 2 vertex endpoints");
            }
            String u = edge.get(0);
            String v = edge.get(1);
            if (!vertexSet.contains(u) || !vertexSet.contains(v)) {
                throw new ApiException(ErrorCode.INVALID_INPUT, "Edge endpoints must be contained in the vertex list");
            }
            edgeSet.add(getCanonicalEdgeKey(u, v));
        }

        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 50;
        List<TraceStep> trace = new ArrayList<>();

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(trace.size() + 1)
                    .action("INIT_COMPLEMENT")
                    .state(Map.of("vertices", vertices.size(), "originalEdges", edges.size(), "targetK", k))
                    .description("Constructing complement graph for " + vertices.size() + " vertices")
                    .build());
        }

        List<List<String>> complementEdges = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            String u = vertices.get(i);
            for (int j = i + 1; j < vertices.size(); j++) {
                String v = vertices.get(j);
                opCount++;
                String key = getCanonicalEdgeKey(u, v);
                if (!edgeSet.contains(key)) {
                    complementEdges.add(List.of(u, v));
                }
            }
        }

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(trace.size() + 1)
                    .action("COMPLEMENT_COMPLETE")
                    .state(Map.of("complementEdges", complementEdges.size()))
                    .description("Created complement graph with " + complementEdges.size() + " edges")
                    .build());
        }

        long endTime = System.nanoTime();

        return CliqueToISResult.builder()
                .sourceProblem("CLIQUE")
                .targetProblem("INDEPENDENT_SET")
                .transformedVertices(vertices)
                .transformedEdges(complementEdges)
                .cliqueSize(k)
                .independentSetSize(k)
                .equivalent(true)
                .trace(trace)
                .executionTimeNanos(endTime - startTime)
                .operationCount(opCount)
                .complexity(getComplexity())
                .build();
    }

    private String getCanonicalEdgeKey(String u, String v) {
        return u.compareTo(v) < 0 ? u + "---" + v : v + "---" + u;
    }
}
