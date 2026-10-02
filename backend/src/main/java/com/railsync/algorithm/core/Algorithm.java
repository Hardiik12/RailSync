package com.railsync.algorithm.core;

import com.railsync.algorithm.common.Complexity;

public interface Algorithm<I, O> {

    O execute(I input);

    String getName();

    Complexity getComplexity();
}
