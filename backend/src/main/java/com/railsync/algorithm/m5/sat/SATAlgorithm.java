package com.railsync.algorithm.m5.sat;

import com.railsync.algorithm.common.Complexity;
import com.railsync.algorithm.common.TraceStep;
import com.railsync.algorithm.core.Algorithm;
import com.railsync.algorithm.m5.common.Literal;
import com.railsync.algorithm.m5.common.SATInput;
import com.railsync.algorithm.m5.common.SATResult;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SATAlgorithm implements Algorithm<SATInput, SATResult> {

    @Override
    public String getName() {
        return "SAT";
    }

    @Override
    public Complexity getComplexity() {
        return new Complexity("O(2^V)", "O(V + C)");
    }

    @Override
    public SATResult execute(SATInput input) {
        long startTime = System.nanoTime();
        long[] opCount = new long[]{0};

        List<String> variables = input.getVariables() != null ? input.getVariables() : new ArrayList<>();
        List<List<Literal>> clauses = input.getNormalizedClauses();

        boolean traceEnabled = Boolean.TRUE.equals(input.getTraceEnabled());
        int maxTraceSteps = input.getMaxTraceSteps() != null ? input.getMaxTraceSteps() : 50;
        List<TraceStep> trace = new ArrayList<>();

        if (variables.isEmpty()) {
            // Deduce variables from clauses if not explicitly provided
            Set<String> varSet = new LinkedHashSet<>();
            for (List<Literal> clause : clauses) {
                for (Literal lit : clause) {
                    varSet.add(lit.getVariable());
                }
            }
            variables = new ArrayList<>(varSet);
        }

        Map<String, Boolean> assignment = new LinkedHashMap<>();
        for (String var : variables) {
            assignment.put(var, null);
        }

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(trace.size() + 1)
                    .action("INIT")
                    .state(Map.of("variables", variables.size(), "clauses", clauses.size()))
                    .description("Initialized DPLL solver with " + variables.size() + " variables and " + clauses.size() + " clauses")
                    .build());
        }

        boolean sat = dpll(clauses, variables, assignment, trace, traceEnabled, maxTraceSteps, opCount);

        long endTime = System.nanoTime();

        Map<String, Boolean> finalAssignment = new LinkedHashMap<>();
        if (sat) {
            for (String v : variables) {
                Boolean val = assignment.get(v);
                finalAssignment.put(v, val != null ? val : true);
            }
        }

        return SATResult.builder()
                .algorithm(getName())
                .satisfiable(sat)
                .assignment(sat ? finalAssignment : null)
                .trace(trace)
                .executionTimeNanos(endTime - startTime)
                .operationCount(opCount[0])
                .complexity(getComplexity())
                .build();
    }

    private boolean dpll(List<List<Literal>> clauses,
                         List<String> variables,
                         Map<String, Boolean> assignment,
                         List<TraceStep> trace,
                         boolean traceEnabled,
                         int maxTraceSteps,
                         long[] opCount) {
        opCount[0]++;

        // Simplify formula based on current assignment
        List<List<Literal>> activeClauses = new ArrayList<>();
        for (List<Literal> clause : clauses) {
            boolean clauseSatisfied = false;
            List<Literal> unassignedLiterals = new ArrayList<>();

            for (Literal lit : clause) {
                Boolean val = assignment.get(lit.getVariable());
                if (val != null) {
                    if ((val && lit.isPositive()) || (!val && !lit.isPositive())) {
                        clauseSatisfied = true;
                        break;
                    }
                } else {
                    unassignedLiterals.add(lit);
                }
            }

            if (!clauseSatisfied) {
                if (unassignedLiterals.isEmpty()) {
                    // Conflict found: empty clause
                    return false;
                }
                activeClauses.add(unassignedLiterals);
            }
        }

        if (activeClauses.isEmpty()) {
            // All clauses satisfied
            return true;
        }

        // Unit Propagation
        boolean changed = true;
        while (changed) {
            changed = false;
            for (List<Literal> clause : activeClauses) {
                if (clause.size() == 1) {
                    Literal unitLit = clause.get(0);
                    if (assignment.get(unitLit.getVariable()) == null) {
                        assignment.put(unitLit.getVariable(), unitLit.isPositive());
                        changed = true;
                        if (traceEnabled && trace.size() < maxTraceSteps) {
                            trace.add(TraceStep.builder()
                                    .step(trace.size() + 1)
                                    .action("UNIT_PROPAGATION")
                                    .state(Map.of("variable", unitLit.getVariable(), "assigned", unitLit.isPositive()))
                                    .description("Unit propagation assigned " + unitLit.getVariable() + " = " + unitLit.isPositive())
                                    .build());
                        }
                        return dpll(clauses, variables, assignment, trace, traceEnabled, maxTraceSteps, opCount);
                    }
                }
            }
        }

        // Pure Literal Elimination
        Map<String, Boolean> polarityMap = new HashMap<>();
        Map<String, Boolean> pureCandidate = new HashMap<>();
        for (List<Literal> clause : activeClauses) {
            for (Literal lit : clause) {
                String var = lit.getVariable();
                if (assignment.get(var) == null) {
                    if (!polarityMap.containsKey(var)) {
                        polarityMap.put(var, lit.isPositive());
                        pureCandidate.put(var, true);
                    } else if (polarityMap.get(var) != lit.isPositive()) {
                        pureCandidate.put(var, false);
                    }
                }
            }
        }

        for (Map.Entry<String, Boolean> entry : pureCandidate.entrySet()) {
            if (Boolean.TRUE.equals(entry.getValue())) {
                String pureVar = entry.getKey();
                boolean pureVal = polarityMap.get(pureVar);
                assignment.put(pureVar, pureVal);
                if (traceEnabled && trace.size() < maxTraceSteps) {
                    trace.add(TraceStep.builder()
                            .step(trace.size() + 1)
                            .action("PURE_LITERAL")
                            .state(Map.of("variable", pureVar, "assigned", pureVal))
                            .description("Pure literal elimination assigned " + pureVar + " = " + pureVal)
                            .build());
                }
                return dpll(clauses, variables, assignment, trace, traceEnabled, maxTraceSteps, opCount);
            }
        }

        // Pick first unassigned variable for branching
        String branchVar = null;
        for (String v : variables) {
            if (assignment.get(v) == null) {
                branchVar = v;
                break;
            }
        }

        if (branchVar == null) {
            return true;
        }

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(trace.size() + 1)
                    .action("BRANCH")
                    .state(Map.of("variable", branchVar, "trying", true))
                    .description("Branching on " + branchVar + " = true")
                    .build());
        }

        // Branch True
        assignment.put(branchVar, true);
        if (dpll(clauses, variables, assignment, trace, traceEnabled, maxTraceSteps, opCount)) {
            return true;
        }

        if (traceEnabled && trace.size() < maxTraceSteps) {
            trace.add(TraceStep.builder()
                    .step(trace.size() + 1)
                    .action("BACKTRACK")
                    .state(Map.of("variable", branchVar, "trying", false))
                    .description("Backtracking: trying " + branchVar + " = false")
                    .build());
        }

        // Branch False
        assignment.put(branchVar, false);
        if (dpll(clauses, variables, assignment, trace, traceEnabled, maxTraceSteps, opCount)) {
            return true;
        }

        // Reset assignment on failure
        assignment.put(branchVar, null);
        return false;
    }
}
