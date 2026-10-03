package com.railsync.algorithm.m5.reductions.clique_is;

import com.railsync.algorithm.m5.common.CliqueToISInput;
import com.railsync.algorithm.m5.common.CliqueToISResult;
import com.railsync.common.error.ApiException;
import com.railsync.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CliqueToISService {

    private final CliqueToIndependentSetReduction reduction;

    public CliqueToISResult execute(CliqueToISInput input) {
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
