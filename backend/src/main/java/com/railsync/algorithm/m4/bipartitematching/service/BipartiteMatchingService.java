package com.railsync.algorithm.m4.bipartitematching.service;

import com.railsync.algorithm.m4.bipartitematching.BipartiteMatchingAlgorithm;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteEdgeDto;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteMatchingInput;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteMatchingResponseDto;
import com.railsync.algorithm.m4.bipartitematching.dto.BipartiteMatchingResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BipartiteMatchingService {

    private static final int MAX_BIPARTITE_VERTICES = 200;

    private final BipartiteMatchingAlgorithm bipartiteMatchingAlgorithm;

    @Value("${railsync.algorithm.max-trace-steps:500}")
    private int defaultMaxTraceSteps;

    public BipartiteMatchingResponseDto execute(BipartiteMatchingInput input) {
        if (input == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Input payload cannot be null");
        }

        List<String> left = input.getLeftVertices();
        List<String> right = input.getRightVertices();

        if (left == null || left.isEmpty() || right == null || right.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameters 'leftVertices' and 'rightVertices' must not be empty");
        }

        int totalVertices = left.size() + right.size();
        if (totalVertices > MAX_BIPARTITE_VERTICES) {
            throw new ApiException(ErrorCode.INPUT_TOO_LARGE, String.format("Total bipartite vertices (%d) exceed maximum limit of %d", totalVertices, MAX_BIPARTITE_VERTICES));
        }

        Set<String> leftSet = new HashSet<>(left);
        Set<String> rightSet = new HashSet<>(right);

        for (String r : rightSet) {
            if (leftSet.contains(r)) {
                throw new ApiException(ErrorCode.INVALID_INPUT, String.format("Vertex '%s' exists in both left and right sets; graph is not valid bipartite", r));
            }
        }

        List<BipartiteEdgeDto> edges = input.getEdges();
        if (edges == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Parameter 'edges' must not be null");
        }

        for (BipartiteEdgeDto edge : edges) {
            if (edge.getLeft() == null || edge.getRight() == null
                    || !leftSet.contains(edge.getLeft()) || !rightSet.contains(edge.getRight())) {
                throw new ApiException(ErrorCode.INVALID_INPUT, String.format("Invalid bipartite edge (%s -> %s)", edge.getLeft(), edge.getRight()));
            }
        }

        int maxSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : defaultMaxTraceSteps;
        if (maxSteps > 2000) {
            throw new ApiException(ErrorCode.TRACE_LIMIT_EXCEEDED, "Requested trace steps exceed maximum allowed limit of 2000");
        }

        BipartiteMatchingInput validatedInput = BipartiteMatchingInput.builder()
                .leftVertices(left)
                .rightVertices(right)
                .edges(edges)
                .traceEnabled(input.getTraceEnabled())
                .maxTraceSteps(maxSteps)
                .build();

        BipartiteMatchingResult result = bipartiteMatchingAlgorithm.execute(validatedInput);
        return BipartiteMatchingResponseDto.fromResult(result, bipartiteMatchingAlgorithm.getName());
    }
}
