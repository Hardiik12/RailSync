package com.railsync.algorithm.m5.reductions.is_vc;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m5.common.ISToVCInput;
import com.railsync.algorithm.m5.common.ISToVCResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class IndependentSetToVertexCoverReduction implements Algorithm<ISToVCInput, ISToVCResult> {

    @Override
    public String getName() {
        return "INDEPENDENT_SET_TO_VERTEX_COVER";
    }

    @Override
    public Complexity getComplexity() {
        return new Complexity("O(V + E)", "O(V)");
    }

    @Override
    public ISToVCResult execute(ISToVCInput input) {
        long startTime = System.nanoTime();
        long opCount = 0;

        List<String> vertices = input.getVertices() != null ? input.getVertices() : new ArrayList<>();
        List<List<String>> edges = input.getEdges() != null ? input.getEdges() : new ArrayList<>();
        Integer inputISSize = input.getIndependentSetSize();
        List<String> providedIS = input.getIndependentSet();

        if (vertices.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Vertices list cannot be empty");
        }

        Set<String> vertexSet = new LinkedHashSet<>(vertices);
        for (List<String> edge : edges) {
            if (edge == null || edge.size() != 2) {
                throw new ApiException(ErrorCode.INVALID_INPUT, "Each edge must contain exactly 2 vertex endpoints");
            }
            if (!vertexSet.contains(edge.get(0)) || !vertexSet.contains(edge.get(1))) {
                throw new ApiException(ErrorCode.INVALID_INPUT, "Edge endpoints must be contained in the vertex list");
            }
        }

        List<String> independentSet = new ArrayList<>();
        if (providedIS != null) {
            // Validation 1 & 2: Check for unknown vertices and duplicates
            Set<String> seenIS = new HashSet<>();
            for (String v : providedIS) {
                if (v == null || !vertexSet.contains(v)) {
                    throw new ApiException(ErrorCode.INVALID_INPUT, "Independent set vertex not present in graph: " + v);
                }
                if (!seenIS.add(v)) {
                    throw new ApiException(ErrorCode.INVALID_INPUT, "Duplicate vertex in supplied independent set: " + v);
                }
                independentSet.add(v);
            }

            // Validation 4: Validate independentSetSize against provided set
            if (inputISSize != null && inputISSize != providedIS.size()) {
                throw new ApiException(ErrorCode.INVALID_INPUT,
                        "Supplied independentSetSize (" + inputISSize + ") does not match actual independent set size (" + providedIS.size() + ")");
            }

            // Validation 3: Check for internal edges in provided independent set
            Set<String> isSet = new HashSet<>(independentSet);
            for (List<String> edge : edges) {
                String u = edge.get(0);
                String v = edge.get(1);
                if (isSet.contains(u) && isSet.contains(v)) {
                    throw new ApiException(ErrorCode.INVALID_INPUT,
                            "Supplied set is not a valid independent set: contains internal edge between " + u + " and " + v);
                }
            }
        } else {
            // Find a maximum independent set by brute force or greedy search for visualization
            independentSet = findMaximumIndependentSet(vertices, edges, opCount);
        }

        int targetISSize = inputISSize != null ? inputISSize : independentSet.size();

        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 50;
        List<TraceStep> trace = new ArrayList<>();

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(trace.size() + 1)
                    .action("INIT_REDUCTION")
                    .state(Map.of("vertices", vertices.size(), "independentSetSize", targetISSize))
                    .description("Initiated Independent Set to Vertex Cover reduction for " + vertices.size() + " vertices")
                    .build());
        }

        Set<String> isSet = new HashSet<>(independentSet);
        List<String> vertexCover = new ArrayList<>();
        for (String v : vertices) {
            opCount++;
            if (!isSet.contains(v)) {
                vertexCover.add(v);
            }
        }

        Set<String> vcSet = new HashSet<>(vertexCover);
        boolean verified = true;
        for (List<String> edge : edges) {
            opCount++;
            String u = edge.get(0);
            String v = edge.get(1);
            if (!vcSet.contains(u) && !vcSet.contains(v)) {
                verified = false;
                break;
            }
        }

        if (independentSet.size() + vertexCover.size() != vertices.size()) {
            verified = false;
        }

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(trace.size() + 1)
                    .action("COMPUTE_COVER")
                    .state(Map.of("isSize", independentSet.size(), "vcSize", vertexCover.size(), "verified", verified))
                    .description("Computed complement vertex cover of size " + vertexCover.size() + " (Verified: " + verified + ")")
                    .build());
        }

        long endTime = System.nanoTime();

        return ISToVCResult.builder()
                .sourceProblem("INDEPENDENT_SET")
                .targetProblem("VERTEX_COVER")
                .vertexCount(vertices.size())
                .independentSetSize(independentSet.size())
                .vertexCoverSize(vertexCover.size())
                .independentSet(independentSet)
                .vertexCover(vertexCover)
                .verified(verified)
                .trace(trace)
                .executionTimeNanos(endTime - startTime)
                .operationCount(opCount)
                .complexity(getComplexity())
                .build();
    }

    private List<String> findMaximumIndependentSet(List<String> vertices, List<List<String>> edges, long opCount) {
        int n = vertices.size();
        if (n > 20) {
            // For larger graphs, greedy heuristic
            Set<String> covered = new HashSet<>();
            List<String> isList = new ArrayList<>();
            for (String v : vertices) {
                if (!covered.contains(v)) {
                    isList.add(v);
                    covered.add(v);
                    for (List<String> edge : edges) {
                        if (edge.get(0).equals(v)) covered.add(edge.get(1));
                        else if (edge.get(1).equals(v)) covered.add(edge.get(0));
                    }
                }
            }
            return isList;
        }

        // Brute force exact max IS for n <= 20
        List<String> bestIS = new ArrayList<>();
        int maxMask = 1 << n;
        for (int mask = 0; mask < maxMask; mask++) {
            List<String> candidate = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    candidate.add(vertices.get(i));
                }
            }

            if (candidate.size() > bestIS.size() && isValidIndependentSet(candidate, edges)) {
                bestIS = candidate;
            }
        }
        return bestIS;
    }

    private boolean isValidIndependentSet(List<String> setNodes, List<List<String>> edges) {
        Set<String> s = new HashSet<>(setNodes);
        for (List<String> edge : edges) {
            if (s.contains(edge.get(0)) && s.contains(edge.get(1))) {
                return false;
            }
        }
        return true;
    }
}
