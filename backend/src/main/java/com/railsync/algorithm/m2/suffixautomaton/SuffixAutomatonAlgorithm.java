package com.railsync.algorithm.m2.suffixautomaton;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m2.suffixautomaton.dto.SAMStateDto;
import com.railsync.algorithm.m2.suffixautomaton.dto.SuffixAutomatonInput;
import com.railsync.algorithm.m2.suffixautomaton.dto.SuffixAutomatonResult;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SuffixAutomatonAlgorithm implements Algorithm<SuffixAutomatonInput, SuffixAutomatonResult> {

    public static class State {
        public int id;
        public int len;
        public int link;
        public boolean isClone;
        public int firstPos;
        public Map<Character, Integer> transitions = new HashMap<>();

        public State(int id, int len, int link, boolean isClone, int firstPos) {
            this.id = id;
            this.len = len;
            this.link = link;
            this.isClone = isClone;
            this.firstPos = firstPos;
        }
    }

    @Override
    public String getName() {
        return "Suffix Automaton (SAM)";
    }

    @Override
    public Complexity getComplexity() {
        return Complexity.builder()
                .time("O(n)")
                .space("O(n * |Sigma|)")
                .build();
    }

    @Override
    public SuffixAutomatonResult execute(SuffixAutomatonInput input) {
        long startTime = System.nanoTime();

        String text = input.getText() != null ? input.getText() : "";
        String query = input.getQuery();
        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 500;

        List<TraceStep> trace = new ArrayList<>();
        List<State> states = new ArrayList<>();

        // Create root state (id = 0, len = 0, link = -1)
        State root = new State(0, 0, -1, false, -1);
        states.add(root);

        int last = 0;
        long operationCount = 0;
        int stepCounter = 1;

        long buildStart = System.nanoTime();

        // Extend automaton character by character
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int cur = states.size();
            State curState = new State(cur, states.get(last).len + 1, -1, false, i);
            states.add(curState);
            operationCount++;

            if (traceEnabled && trace.size() < maxTraceSteps) {
                trace.add(TraceStep.builder()
                        .step(stepCounter++)
                        .action("SAM_STATE_CREATED")
                        .state(Map.of("char", String.valueOf(c), "stateId", cur, "len", curState.len))
                        .description(String.format("Created SAM state #%d for char '%c' (max len: %d)", cur, c, curState.len))
                        .build());
            }

            int p = last;
            while (p != -1 && !states.get(p).transitions.containsKey(c)) {
                states.get(p).transitions.put(c, cur);
                operationCount++;
                p = states.get(p).link;
            }

            if (p == -1) {
                curState.link = 0; // link to root
            } else {
                int q = states.get(p).transitions.get(c);
                if (states.get(p).len + 1 == states.get(q).len) {
                    curState.link = q;
                } else {
                    int clone = states.size();
                    State qState = states.get(q);
                    State cloneState = new State(clone, states.get(p).len + 1, qState.link, true, qState.firstPos);
                    cloneState.transitions = new HashMap<>(qState.transitions);
                    states.add(cloneState);
                    operationCount++;

                    if (traceEnabled && trace.size() < maxTraceSteps) {
                        trace.add(TraceStep.builder()
                                .step(stepCounter++)
                                .action("SAM_CLONE_CREATED")
                                .state(Map.of("cloneId", clone, "originalId", q, "cloneLen", cloneState.len))
                                .description(String.format("Created clone state #%d from state #%d with len %d", clone, q, cloneState.len))
                                .build());
                    }

                    while (p != -1 && states.get(p).transitions.get(c) == q) {
                        states.get(p).transitions.put(c, clone);
                        operationCount++;
                        p = states.get(p).link;
                    }

                    qState.link = clone;
                    curState.link = clone;
                }
            }
            last = cur;
        }

        long buildEnd = System.nanoTime();

        // Query execution
        long queryStart = System.nanoTime();
        boolean substringFound = false;
        int firstOccurrenceIndex = -1;

        if (query != null && !query.isEmpty() && !text.isEmpty()) {
            int currentState = 0;
            boolean matchFailed = false;

            for (int i = 0; i < query.length(); i++) {
                char c = query.charAt(i);
                operationCount++;
                if (states.get(currentState).transitions.containsKey(c)) {
                    currentState = states.get(currentState).transitions.get(c);
                } else {
                    matchFailed = true;
                    break;
                }
            }

            if (!matchFailed) {
                substringFound = true;
                State finalState = states.get(currentState);
                firstOccurrenceIndex = finalState.firstPos - query.length() + 1;
            }
        }
        long queryEnd = System.nanoTime();
        long totalEndTime = System.nanoTime();

        // Convert States to DTOs for UI visualization
        List<SAMStateDto> stateDtos = new ArrayList<>();
        for (State s : states) {
            Map<String, Integer> transMap = new HashMap<>();
            for (Map.Entry<Character, Integer> entry : s.transitions.entrySet()) {
                transMap.put(String.valueOf(entry.getKey()), entry.getValue());
            }

            stateDtos.add(SAMStateDto.builder()
                    .id(s.id)
                    .len(s.len)
                    .link(s.link)
                    .isClone(s.isClone)
                    .firstPos(s.firstPos)
                    .transitions(transMap)
                    .build());
        }

        return SuffixAutomatonResult.builder()
                .text(text)
                .query(query)
                .substringFound(substringFound)
                .firstOccurrenceIndex(firstOccurrenceIndex)
                .stateCount(states.size())
                .automatonStates(stateDtos)
                .buildNanos(buildEnd - buildStart)
                .queryNanos(queryEnd - queryStart)
                .executionTimeNanos(totalEndTime - startTime)
                .operationCount(operationCount)
                .complexity(getComplexity())
                .trace(trace)
                .build();
    }
}
