package com.railsync.algorithm.m5.reductions.is_vc;

import com.railsync.algorithm.m5.common.ISToVCInput;
import com.railsync.algorithm.m5.common.ISToVCResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ISToVCService {

    private final IndependentSetToVertexCoverReduction reduction;

    public ISToVCResult execute(ISToVCInput input) {
        if (input == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Input cannot be null");
        }

        List<String> vertices = input.getVertices();
        if (vertices == null || vertices.isEmpty()) {
            throw new ApiException(ErrorCode.INVALID_INPUT, "Vertices list cannot be empty");
        }

        if (vertices.size() > 100) {
            throw new ApiException(ErrorCode.INPUT_TOO_LARGE,
                    "Graph size limit exceeded. Max 100 vertices allowed for reduction.");
        }

        return reduction.execute(input);
    }
}
