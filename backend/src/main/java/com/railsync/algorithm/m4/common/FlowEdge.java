package com.railsync.algorithm.m4.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowEdge {
    private int u;
    private int v;
    private double capacity;
    private double flow;
    private String uName;
    private String vName;
    private FlowEdge reverseEdge;

    /**
     * Calculates residual capacity to the specified target node.
     * Invariants:
     * 0 <= flow <= capacity for forward edge
     * Forward residual capacity = capacity - flow
     * Reverse residual capacity = flow (allowing flow cancellation)
     */
    public double residualCapacityTo(int target) {
        if (target == v) {
            return (capacity - flow) + (reverseEdge != null ? reverseEdge.flow : 0.0);
        } else if (target == u) {
            return flow + (reverseEdge != null ? (reverseEdge.capacity - reverseEdge.flow) : 0.0);
        }
        throw new IllegalArgumentException("Target node " + target + " invalid for edge (" + u + "->" + v + ")");
    }

    /**
     * Pushes delta residual flow toward the specified target node.
     * Flow cancellation on reverse edge is performed first before increasing forward flow.
     */
    public void addResidualFlowTo(int target, double delta) {
        if (target == v) {
            if (reverseEdge != null && reverseEdge.flow > 0) {
                double reduce = Math.min(delta, reverseEdge.flow);
                reverseEdge.flow -= reduce;
                delta -= reduce;
            }
            flow += delta;
        } else if (target == u) {
            if (flow > 0) {
                double reduce = Math.min(delta, flow);
                flow -= reduce;
                delta -= reduce;
            }
            if (reverseEdge != null && delta > 0) {
                reverseEdge.flow += delta;
            }
        } else {
            throw new IllegalArgumentException("Target node " + target + " invalid for edge (" + u + "->" + v + ")");
        }
    }
}
