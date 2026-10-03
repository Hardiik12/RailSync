package com.railsync.algorithm.m5.approx;

import com.railsync.algorithm.m5.common.VertexCoverApproxInput;
import com.railsync.algorithm.m5.common.VertexCoverApproxResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VertexCoverApproxService {

    private final VertexCoverTwoApproximation approxAlgorithm;

    public VertexCoverApproxResult execute(VertexCoverApproxInput input) {
        if (input == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Input cannot be null");
        }

        List<String> vertices = input.getVertices();
        if (vertices == null || vertices.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Vertices list cannot be empty");
        }

        if (vertices.size() > 500) {
            throw new ApiException(ErrorCode.INPUT_TOO_LARGE,
                    "Graph size limit exceeded. Max 500 vertices allowed for 2-approximation.");
        }

        return approxAlgorithm.execute(input);
    }
}
