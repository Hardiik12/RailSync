package com.railsync.algorithm.m4.common;

import java.util.ArrayList;
import java.util.List;

public class FlowNetwork {
    private final int vertexCount;
    private final List<List<FlowEdge>> adj;
    private final List<FlowEdge> edges;

    public FlowNetwork(int vertexCount) {
        this.vertexCount = vertexCount;
        this.adj = new ArrayList<>(vertexCount);
        this.edges = new ArrayList<>();
        for (int i = 0; i < vertexCount; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void addEdge(int u, int v, double capacity, String uName, String vName) {
        String uLabel = (uName != null && !uName.isBlank()) ? uName : "Node " + u;
        String vLabel = (vName != null && !vName.isBlank()) ? vName : "Node " + v;

        FlowEdge forward = FlowEdge.builder()
                .u(u)
                .v(v)
                .capacity(capacity)
                .flow(0.0)
                .uName(uLabel)
                .vName(vLabel)
                .build();
        FlowEdge reverse = FlowEdge.builder()
                .u(v)
                .v(u)
                .capacity(0.0)
                .flow(0.0)
                .uName(vLabel)
                .vName(uLabel)
                .build();

        forward.setReverseEdge(reverse);
        reverse.setReverseEdge(forward);

        adj.get(u).add(forward);
        adj.get(v).add(reverse);
        edges.add(forward);
    }

    public List<FlowEdge> getAdj(int u) {
        return adj.get(u);
    }

    public List<FlowEdge> getEdges() {
        return edges;
    }
}
