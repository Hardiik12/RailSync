package com.railsync.algorithm.m5;

import com.railsync.algorithm.m5.approx.VertexCoverTwoApproximation;
import com.railsync.algorithm.m5.common.*;
import com.railsync.algorithm.m5.reductions.clique_is.CliqueToIndependentSetReduction;
import com.railsync.algorithm.m5.reductions.is_vc.IndependentSetToVertexCoverReduction;
import com.railsync.algorithm.m5.reductions.threesat_clique.ThreeSATToCliqueReduction;
import com.railsync.algorithm.m5.sat.SATAlgorithm;
import com.railsync.algorithm.m5.threesat.ThreeSATAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class M5CrossValidationTest {

    private SATAlgorithm satAlgorithm;
    private ThreeSATAlgorithm threeSATAlgorithm;
    private ThreeSATToCliqueReduction threeSATToCliqueReduction;
    private CliqueToIndependentSetReduction cliqueToISReduction;
    private IndependentSetToVertexCoverReduction isToVCReduction;
    private VertexCoverTwoApproximation vcTwoApprox;

    @BeforeEach
    void setUp() {
        satAlgorithm = new SATAlgorithm();
        threeSATAlgorithm = new ThreeSATAlgorithm(satAlgorithm);
        threeSATToCliqueReduction = new ThreeSATToCliqueReduction(satAlgorithm);
        cliqueToISReduction = new CliqueToIndependentSetReduction();
        isToVCReduction = new IndependentSetToVertexCoverReduction();
        vcTwoApprox = new VertexCoverTwoApproximation();
    }

    @Test
    void crossValidateReductionChain() {
        // 1. 3-SAT Formula
        SATInput satInput = SATInput.builder()
                .variables(List.of("A", "B", "C"))
                .stringClauses(List.of(
                        List.of("A", "!B", "C"),
                        List.of("!A", "B", "C")
                ))
                .build();

        // Solve 3-SAT
        SATResult satRes = threeSATAlgorithm.execute(satInput);
        assertTrue(satRes.isSatisfiable());

        // 2. Reduce 3-SAT to CLIQUE
        CliqueReductionResult cliqueRes = threeSATToCliqueReduction.execute(satInput);
        assertTrue(cliqueRes.isSatisfiable());
        assertEquals(2, cliqueRes.getTargetCliqueSize());
        assertFalse(cliqueRes.getCliqueFound().isEmpty());

        // 3. Reduce CLIQUE to Independent Set
        CliqueToISInput isInput = CliqueToISInput.builder()
                .vertices(cliqueRes.getVertices())
                .edges(cliqueRes.getEdges())
                .cliqueSize(cliqueRes.getTargetCliqueSize())
                .build();

        CliqueToISResult isRes = cliqueToISReduction.execute(isInput);
        assertTrue(isRes.isEquivalent());
        assertEquals(cliqueRes.getTargetCliqueSize(), isRes.getIndependentSetSize());

        // 4. Reduce Independent Set to Vertex Cover
        ISToVCInput vcInput = ISToVCInput.builder()
                .vertices(isRes.getTransformedVertices())
                .edges(isRes.getTransformedEdges())
                .independentSetSize(isRes.getIndependentSetSize())
                .build();

        ISToVCResult vcRes = isToVCReduction.execute(vcInput);
        assertTrue(vcRes.isVerified());
        assertEquals(isRes.getTransformedVertices().size(), vcRes.getIndependentSetSize() + vcRes.getVertexCoverSize());

        // 5. Run Vertex Cover 2-Approximation on the transformed graph
        VertexCoverApproxInput approxInput = VertexCoverApproxInput.builder()
                .vertices(isRes.getTransformedVertices())
                .edges(isRes.getTransformedEdges())
                .build();

        VertexCoverApproxResult approxRes = vcTwoApprox.execute(approxInput);
        assertTrue(approxRes.isVerifiedCover());
        assertTrue(approxRes.getCoverSize() <= 2 * vcRes.getVertexCoverSize());
    }
}
