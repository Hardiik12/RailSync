package com.railsync.algorithm.m5.approx;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m5.common.VertexCoverApproxInput;
import com.railsync.algorithm.m5.common.VertexCoverApproxResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class VertexCoverTwoApproximation implements Algorithm<VertexCoverApproxInput, VertexCoverApproxResult> {

    @Override
    public String getName() {
        return "VERTEX_COVER_2_APPROX";
    }

    @Override
    public Complexity getComplexity() {
        return new Complexity("O(V + E)", "O(V + E)");
    }

    @Override
    public VertexCoverApproxResult execute(VertexCoverApproxInput input) {
        long startTime = System.nanoTime();
        long opCount = 0;

        List<String> vertices = input.getVertices() != null ? input.getVertices() : new ArrayList<>();
        List<List<String>> edges = input.getEdges() != null ? input.getEdges() : new ArrayList<>();

        if (vertices.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Vertices list cannot be empty");
        }

        Set<String> vertexSet = new HashSet<>(vertices);
        List<List<String>> validatedEdges = new ArrayList<>();
        for (List<String> edge : edges) {
            if (edge == null || edge.size() != 2) {
                throw new ApiException(ErrorCode.INVALID_INPUT, "Each edge must contain exactly 2 vertex endpoints");
            }
            String u = edge.get(0);
            String v = edge.get(1);
            if (!vertexSet.contains(u) || !vertexSet.contains(v)) {
                throw new ApiException(ErrorCode.INVALID_INPUT, "Edge endpoints must be contained in the vertex list");
            }
            validatedEdges.add(List.of(u, v));
        }

        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 50;
        List<TraceStep> trace = new ArrayList<>();

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(trace.size() + 1)
                    .action("INIT")
                    .state(Map.of("vertices", vertices.size(), "edges", validatedEdges.size()))
                    .description("Initiated 2-Approximation Vertex Cover algorithm")
                    .build());
        }

        Set<String> cover = new LinkedHashSet<>();
        List<List<String>> selectedEdges = new ArrayList<>();
        List<List<String>> activeEdges = new ArrayList<>(validatedEdges);

        while (!activeEdges.isEmpty()) {
            opCount++;
            List<String> edge = activeEdges.remove(0);
            String u = edge.get(0);
            String v = edge.get(1);

            cover.add(u);
            cover.add(v);
            selectedEdges.add(edge);

            if (traceEnabled && trace.size() < maxTraceSteps) {
                trace.add(TraceStep.builder()
                        .step(trace.size() + 1)
                        .action("SELECT_EDGE")
                        .state(Map.of("selectedEdge", edge, "addedVertices", List.of(u, v), "currentCoverSize", cover.size()))
                        .description("Selected edge (" + u + ", " + v + ") and added endpoints to cover")
                        .build());
            }

            // Remove all edges incident to u or v
            activeEdges.removeIf(e -> {
                boolean incident = e.get(0).equals(u) || e.get(1).equals(u) || e.get(0).equals(v) || e.get(1).equals(v);
                return incident;
            });
        }

        // Verify that cover touches every original edge
        boolean isVerifiedCover = true;
        for (List<String> edge : validatedEdges) {
            opCount++;
            if (!cover.contains(edge.get(0)) && !cover.contains(edge.get(1))) {
                isVerifiedCover = false;
                break;
            }
        }

        long endTime = System.nanoTime();

        Map<String, Object> bound = Map.of(
                "maximumAllowedRatio", 2.0,
                "theoreticalGuarantee", "|C| <= 2 * OPT",
                "approximationMethod", "Maximal-Matching-Based 2-Approximation"
        );

        return VertexCoverApproxResult.builder()
                .algorithm(getName())
                .cover(new ArrayList<>(cover))
                .coverSize(cover.size())
                .edgeCount(validatedEdges.size())
                .selectedEdges(selectedEdges)
                .bound(bound)
                .isVerifiedCover(isVerifiedCover)
                .trace(trace)
                .executionTimeNanos(endTime - startTime)
                .operationCount(opCount)
                .complexity(getComplexity())
                .build();
    }
}
