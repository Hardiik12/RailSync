package com.railsync.algorithm.m4.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EdgeInputDto {
    private Integer u;
    private Integer v;
    private Double capacity;
    private String uName;
    private String vName;
}
