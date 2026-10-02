package com.railsync.algorithm.m1.ahocorasick;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m1.ahocorasick.dto.AhoCorasickInput;
import com.railsync.algorithm.m1.ahocorasick.dto.AhoCorasickResult;
import com.railsync.algorithm.m1.ahocorasick.dto.KeywordMatchDto;
import com.railsync.algorithm.m1.ahocorasick.dto.TrieNodeDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AhoCorasickAlgorithm implements Algorithm<AhoCorasickInput, AhoCorasickResult> {

    public static class Node {
        public final int id;
        public final char ch;
        public final int parentId;
        public final Map<Character, Node> children = new HashMap<>();
        public Node failLink = null;
        public final List<String> outputs = new ArrayList<>();

        public Node(int id, char ch, int parentId) {
            this.id = id;
            this.ch = ch;
            this.parentId = parentId;
        }
    }

    @Override
    public String getName() {
        return "Aho-Corasick Multi-Pattern Algorithm";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(n + sum(|P_i|) + Z)")
                .space("O(sum(|P_i|) * |Sigma|)")
                .build();
    }

    @Override
    public AhoCorasickResult execute(AhoCorasickInput input) {
        long totalStartTime = System.nanoTime();

        String text = input.getText() != null ? input.getText() : "";
        List<String> keywords = input.getKeywords() != null ? input.getKeywords() : Collections.emptyList();
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();
        List<KeywordMatchDto> matches = new ArrayList<>();
        Set<String> matchedKeywordSet = new HashSet<>();
        long operationCount = 0;
        int stepCounter = 1;

        // Step 1: Build Trie
        long trieBuildStart = System.nanoTime();
        int nodeCounter = 0;
        Node root = new Node(nodeCounter++, '\0', -1);
        root.failLink = root;

        for (String kw : keywords) {
            if (kw == null || kw.isEmpty()) continue;
            Node current = root;
            for (int i = 0; i < kw.length(); i++) {
                char c = kw.charAt(i);
                operationCount++;
                if (!current.children.containsKey(c)) {
                    Node childNode = new Node(nodeCounter++, c, current.id);
                    current.children.put(c, childNode);
                    if (traceEnabled && trace.size() < maxTraceSteps) {
                        trace.add(TraceStep.builder()
                                .step(stepCounter++)
                                .action("TRIE_ADD_NODE")
                                .state(Map.of("nodeId", childNode.id, "char", String.valueOf(c), "parentId", current.id, "keyword", kw))
                                .description(String.format("Created trie node #%d for char '%c' (parent #%d) for keyword '%s'", childNode.id, c, current.id, kw))
                                .build());
                    }
                }
                current = current.children.get(c);
            }
            current.outputs.add(kw);
        }

        // Step 2: Build Failure Links via BFS
        Queue<Node> queue = new ArrayDeque<>();
        for (Node child : root.children.values()) {
            child.failLink = root;
            queue.add(child);
            operationCount++;
            if (traceEnabled && trace.size() < maxTraceSteps) {
                trace.add(TraceStep.builder()
                        .step(stepCounter++)
                        .action("FAIL_LINK_INIT")
                        .state(Map.of("nodeId", child.id, "failNodeId", root.id, "char", String.valueOf(child.ch)))
                        .description(String.format("Set depth-1 node #%d ('%c') failure link to root #0", child.id, child.ch))
                        .build());
            }
        }

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            for (Map.Entry<Character, Node> entry : current.children.entrySet()) {
                char c = entry.getKey();
                Node child = entry.getValue();
                queue.add(child);

                Node failCandidate = current.failLink;
                while (failCandidate != root && !failCandidate.children.containsKey(c)) {
                    failCandidate = failCandidate.failLink;
                    operationCount++;
                }

                if (failCandidate.children.containsKey(c) && failCandidate.children.get(c) != child) {
                    child.failLink = failCandidate.children.get(c);
                } else {
                    child.failLink = root;
                }
                operationCount++;

                // Merge outputs from failure link node
                child.outputs.addAll(child.failLink.outputs);

                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(stepCounter++)
                            .action("FAIL_LINK_BUILD")
                            .state(Map.of("nodeId", child.id, "failNodeId", child.failLink.id, "char", String.valueOf(c), "outputCount", child.outputs.size()))
                            .description(String.format("Set failure link for node #%d ('%c') -> node #%d (outputs: %s)", child.id, c, child.failLink.id, child.outputs))
                            .build());
                }
            }
        }

        long trieBuildEnd = System.nanoTime();

        // Step 3: Single Traversal of Text
        long searchStart = System.nanoTime();
        Node currentState = root;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            while (currentState != root && !currentState.children.containsKey(c)) {
                currentState = currentState.failLink;
                operationCount++;
            }

            if (currentState.children.containsKey(c)) {
                currentState = currentState.children.get(c);
            } else {
                currentState = root;
            }
            operationCount++;

            if (!currentState.outputs.isEmpty()) {
                for (String matchedKw : currentState.outputs) {
                    int startIndex = i - matchedKw.length() + 1;
                    int endIndex = i;
                    matches.add(KeywordMatchDto.builder()
                            .keyword(matchedKw)
                            .startIndex(startIndex)
                            .endIndex(endIndex)
                            .build());
                    matchedKeywordSet.add(matchedKw);

                    if (traceEnabled && trace.size() < maxTraceSteps) {
                        trace.add(TraceStep.builder()
                                .step(stepCounter++)
                                .action("KEYWORD_MATCH")
                                .state(Map.of("keyword", matchedKw, "startIndex", startIndex, "endIndex", endIndex, "nodeId", currentState.id))
                                .description(String.format("Matched alert keyword '%s' at text indices [%d..%d] (state node #%d)", matchedKw, startIndex, endIndex, currentState.id))
                                .build());
                    }
                }
            } else {
                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(stepCounter++)
                            .action("STATE_TRANSITION")
                            .state(Map.of("textIndex", i, "char", String.valueOf(c), "nodeId", currentState.id))
                            .description(String.format("Transitioned to state node #%d on character '%c' at index %d", currentState.id, c, i))
                            .build());
                }
            }
        }

        long searchEnd = System.nanoTime();
        long totalEndTime = System.nanoTime();

        // Convert Node tree to DTO list for UI visualization
        List<TrieNodeDto> nodeDtos = collectNodeDtos(root);

        return AhoCorasickResult.builder()
                .matches(matches)
                .matchCount(matches.size())
                .matchedKeywords(new ArrayList<>(matchedKeywordSet))
                .automatonNodes(nodeDtos)
                .trieBuildNanos(trieBuildEnd - trieBuildStart)
                .searchNanos(searchEnd - searchStart)
                .executionTimeNanos(totalEndTime - totalStartTime)
                .operationCount(operationCount)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }

    private List<TrieNodeDto> collectNodeDtos(Node root) {
        List<TrieNodeDto> list = new ArrayList<>();
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node n = queue.poll();
            Map<String, Integer> trans = new HashMap<>();
            for (Map.Entry<Character, Node> entry : n.children.entrySet()) {
                trans.put(String.valueOf(entry.getKey()), entry.getValue().id);
                queue.add(entry.getValue());
            }

            list.add(TrieNodeDto.builder()
                    .id(n.id)
                    .charLabel(n.ch)
                    .parentId(n.parentId)
                    .failLink(n.failLink != null ? n.failLink.id : 0)
                    .transitions(trans)
                    .outputs(n.outputs)
                    .isRoot(n.id == 0)
                    .build());
        }
        return list;
    }
}
