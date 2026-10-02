package com.railsync.algorithm.m3.levenshtein.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditOperationDto {
    private String type; // KEEP, INSERT, DELETE, SUBSTITUTE, TRANSPOSITION
    private Character sourceChar;
    private Character targetChar;
    private int sourceIndex;
    private int targetIndex;
    private String description;
}
