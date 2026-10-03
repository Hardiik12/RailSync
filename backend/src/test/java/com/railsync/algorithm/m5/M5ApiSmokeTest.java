package com.railsync.algorithm.m5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.railsync.algorithm.m5.approx.VertexCoverApproxController;
import com.railsync.algorithm.m5.approx.VertexCoverApproxService;
import com.railsync.algorithm.m5.approx.VertexCoverTwoApproximation;
import com.railsync.algorithm.m5.common.*;
import com.railsync.algorithm.m5.reductions.clique_is.CliqueToISController;
import com.railsync.algorithm.m5.reductions.clique_is.CliqueToISService;
import com.railsync.algorithm.m5.reductions.clique_is.CliqueToIndependentSetReduction;
import com.railsync.algorithm.m5.reductions.is_vc.ISToVCController;
import com.railsync.algorithm.m5.reductions.is_vc.ISToVCService;
import com.railsync.algorithm.m5.reductions.is_vc.IndependentSetToVertexCoverReduction;
import com.railsync.algorithm.m5.reductions.threesat_clique.ThreeSATToCliqueController;
import com.railsync.algorithm.m5.reductions.threesat_clique.ThreeSATToCliqueReduction;
import com.railsync.algorithm.m5.reductions.threesat_clique.ThreeSATToCliqueService;
import com.railsync.algorithm.m5.sat.SATAlgorithm;
import com.railsync.algorithm.m5.sat.SATController;
import com.railsync.algorithm.m5.sat.SATService;
import com.railsync.algorithm.m5.threesat.ThreeSATAlgorithm;
import com.railsync.algorithm.m5.threesat.ThreeSATController;
import com.railsync.algorithm.m5.threesat.ThreeSATService;
import com.railsync.common.error.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class M5ApiSmokeTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        SATAlgorithm satAlgorithm = new SATAlgorithm();
        SATService satService = new SATService(satAlgorithm);
        SATController satController = new SATController(satService);

        ThreeSATAlgorithm threeSATAlgorithm = new ThreeSATAlgorithm(satAlgorithm);
        ThreeSATService threeSATService = new ThreeSATService(threeSATAlgorithm);
        ThreeSATController threeSATController = new ThreeSATController(threeSATService);

        ThreeSATToCliqueReduction threeSATToCliqueReduction = new ThreeSATToCliqueReduction(satAlgorithm);
        ThreeSATToCliqueService threeSATToCliqueService = new ThreeSATToCliqueService(threeSATToCliqueReduction);
        ThreeSATToCliqueController threeSATToCliqueController = new ThreeSATToCliqueController(threeSATToCliqueService);

        CliqueToIndependentSetReduction cliqueToISReduction = new CliqueToIndependentSetReduction();
        CliqueToISService cliqueToISService = new CliqueToISService(cliqueToISReduction);
        CliqueToISController cliqueToISController = new CliqueToISController(cliqueToISService);

        IndependentSetToVertexCoverReduction isToVCReduction = new IndependentSetToVertexCoverReduction();
        ISToVCService isToVCService = new ISToVCService(isToVCReduction);
        ISToVCController isToVCController = new ISToVCController(isToVCService);

        VertexCoverTwoApproximation vcTwoApprox = new VertexCoverTwoApproximation();
        VertexCoverApproxService vcApproxService = new VertexCoverApproxService(vcTwoApprox);
        VertexCoverApproxController vcApproxController = new VertexCoverApproxController(vcApproxService);

        mockMvc = MockMvcBuilders.standaloneSetup(
                satController,
                threeSATController,
                threeSATToCliqueController,
                cliqueToISController,
                isToVCController,
                vcApproxController
        ).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    // ==========================================
    // A. SAT API Tests
    // ==========================================

    @Test
    void testSatApiSatisfiable() throws Exception {
        SATInput input = SATInput.builder()
                .variables(List.of("T1_P1", "T1_P2", "T2_P1"))
                .stringClauses(List.of(
                        List.of("T1_P1"),
                        List.of("!T1_P1", "T1_P2"),
                        List.of("!T2_P1")
                ))
                .traceEnabled(true)
                .build();

        mockMvc.perform(post("/api/m5/sat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.satisfiable").value(true))
                .andExpect(jsonPath("$.data.assignment.T1_P1").value(true))
                .andExpect(jsonPath("$.data.assignment.T2_P1").value(false))
                .andExpect(jsonPath("$.meta.algorithm").value("SAT"))
                .andExpect(jsonPath("$.meta.requestId").exists())
                .andExpect(jsonPath("$.meta.timestamp").exists());
    }

    @Test
    void testSatApiUnsatisfiable() throws Exception {
        SATInput input = SATInput.builder()
                .variables(List.of("A"))
                .stringClauses(List.of(List.of("A"), List.of("!A")))
                .build();

        mockMvc.perform(post("/api/m5/sat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.satisfiable").value(false));
    }

    @Test
    void testSatApiMalformedInput() throws Exception {
        mockMvc.perform(post("/api/m5/sat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clauses\": []}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"));
    }

    // ==========================================
    // B. 3-SAT API Tests
    // ==========================================

    @Test
    void testThreeSatApiValidSatisfiable() throws Exception {
        SATInput input = SATInput.builder()
                .variables(List.of("A", "B", "C"))
                .stringClauses(List.of(
                        List.of("A", "!B", "C"),
                        List.of("!A", "B", "C")
                ))
                .build();

        mockMvc.perform(post("/api/m5/3sat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.satisfiable").value(true))
                .andExpect(jsonPath("$.meta.algorithm").value("3-SAT"));
    }

    @Test
    void testThreeSatApiClauseFewerThan3Literals() throws Exception {
        SATInput input = SATInput.builder()
                .variables(List.of("A", "B", "C"))
                .stringClauses(List.of(List.of("A", "B"))) // 2 literals
                .build();

        mockMvc.perform(post("/api/m5/3sat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"));
    }

    @Test
    void testThreeSatApiClauseMoreThan3Literals() throws Exception {
        SATInput input = SATInput.builder()
                .variables(List.of("A", "B", "C", "D"))
                .stringClauses(List.of(List.of("A", "B", "C", "D"))) // 4 literals
                .build();

        mockMvc.perform(post("/api/m5/3sat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"));
    }

    // ==========================================
    // C. 3-SAT → CLIQUE Reduction API Tests
    // ==========================================

    @Test
    void testThreeSatToCliqueApi() throws Exception {
        SATInput input = SATInput.builder()
                .variables(List.of("A", "B", "C"))
                .stringClauses(List.of(
                        List.of("A", "!B", "C"),
                        List.of("!A", "B", "C")
                ))
                .build();

        mockMvc.perform(post("/api/m5/3sat-to-clique")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.sourceProblem").value("3-SAT"))
                .andExpect(jsonPath("$.data.targetProblem").value("CLIQUE"))
                .andExpect(jsonPath("$.data.targetCliqueSize").value(2))
                .andExpect(jsonPath("$.data.vertices.length()").value(6))
                .andExpect(jsonPath("$.data.satisfiable").value(true));
    }

    // ==========================================
    // D. CLIQUE → Independent Set API Tests
    // ==========================================

    @Test
    void testCliqueToISApi() throws Exception {
        CliqueToISInput input = CliqueToISInput.builder()
                .vertices(List.of("A", "B", "C", "D"))
                .edges(List.of(
                        List.of("A", "B"),
                        List.of("B", "C"),
                        List.of("A", "C")
                ))
                .cliqueSize(3)
                .build();

        mockMvc.perform(post("/api/m5/clique-to-independent-set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.sourceProblem").value("CLIQUE"))
                .andExpect(jsonPath("$.data.targetProblem").value("INDEPENDENT_SET"))
                .andExpect(jsonPath("$.data.independentSetSize").value(3))
                .andExpect(jsonPath("$.data.equivalent").value(true));
    }

    @Test
    void testCliqueToISApiInvalidVertexInEdge() throws Exception {
        CliqueToISInput input = CliqueToISInput.builder()
                .vertices(List.of("A", "B"))
                .edges(List.of(List.of("A", "UNKNOWN")))
                .cliqueSize(2)
                .build();

        mockMvc.perform(post("/api/m5/clique-to-independent-set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"));
    }

    // ==========================================
    // E. Independent Set → Vertex Cover API Tests
    // ==========================================

    @Test
    void testISToVCApiValid() throws Exception {
        ISToVCInput input = ISToVCInput.builder()
                .vertices(List.of("A", "B", "C", "D", "E"))
                .edges(List.of(
                        List.of("A", "B"),
                        List.of("B", "C"),
                        List.of("C", "D"),
                        List.of("D", "E")
                ))
                .independentSetSize(3)
                .independentSet(List.of("A", "C", "E"))
                .build();

        mockMvc.perform(post("/api/m5/independent-set-to-vertex-cover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.vertexCount").value(5))
                .andExpect(jsonPath("$.data.independentSetSize").value(3))
                .andExpect(jsonPath("$.data.vertexCoverSize").value(2))
                .andExpect(jsonPath("$.data.verified").value(true));
    }

    @Test
    void testISToVCApiUnknownVertex() throws Exception {
        ISToVCInput input = ISToVCInput.builder()
                .vertices(List.of("A", "B"))
                .edges(List.of(List.of("A", "B")))
                .independentSet(List.of("UNKNOWN"))
                .build();

        mockMvc.perform(post("/api/m5/independent-set-to-vertex-cover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"));
    }

    @Test
    void testISToVCApiDuplicateVertex() throws Exception {
        ISToVCInput input = ISToVCInput.builder()
                .vertices(List.of("A", "B"))
                .edges(List.of(List.of("A", "B")))
                .independentSet(List.of("A", "A"))
                .build();

        mockMvc.perform(post("/api/m5/independent-set-to-vertex-cover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"));
    }

    @Test
    void testISToVCApiInternalEdge() throws Exception {
        ISToVCInput input = ISToVCInput.builder()
                .vertices(List.of("A", "B", "C"))
                .edges(List.of(List.of("A", "B")))
                .independentSet(List.of("A", "B")) // Edge A-B internal to IS
                .build();

        mockMvc.perform(post("/api/m5/independent-set-to-vertex-cover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"));
    }

    @Test
    void testISToVCApiSizeMismatch() throws Exception {
        ISToVCInput input = ISToVCInput.builder()
                .vertices(List.of("A", "B", "C"))
                .edges(List.of(List.of("A", "B")))
                .independentSetSize(10)
                .independentSet(List.of("A", "C"))
                .build();

        mockMvc.perform(post("/api/m5/independent-set-to-vertex-cover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("INVALID_INPUT"));
    }

    // ==========================================
    // F. Vertex Cover 2-Approximation API Tests
    // ==========================================

    @Test
    void testVertexCover2ApproxApi() throws Exception {
        VertexCoverApproxInput input = VertexCoverApproxInput.builder()
                .vertices(List.of("A", "B", "C", "D"))
                .edges(List.of(
                        List.of("A", "B"),
                        List.of("B", "C"),
                        List.of("C", "D")
                ))
                .build();

        mockMvc.perform(post("/api/m5/vertex-cover-2approx")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.algorithm").value("VERTEX_COVER_2_APPROX"))
                .andExpect(jsonPath("$.data.isVerifiedCover").value(true))
                .andExpect(jsonPath("$.data.bound.maximumAllowedRatio").value(2.0));
    }

    // ==========================================
    // Full Reduction Chain End-to-End Test
    // ==========================================

    @Test
    void testCompleteReductionChainApiEndToEnd() throws Exception {
        // Stage 1: 3-SAT
        SATInput sat3Input = SATInput.builder()
                .variables(List.of("A", "B", "C"))
                .stringClauses(List.of(
                        List.of("A", "!B", "C"),
                        List.of("!A", "B", "C")
                ))
                .build();

        MvcResult res1 = mockMvc.perform(post("/api/m5/3sat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sat3Input)))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(res1.getResponse().getContentAsString().contains("\"satisfiable\":true"));

        // Stage 2: 3-SAT -> CLIQUE
        MvcResult res2 = mockMvc.perform(post("/api/m5/3sat-to-clique")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sat3Input)))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> map2 = objectMapper.readValue(res2.getResponse().getContentAsString(), Map.class);
        Map<String, Object> data2 = (Map<String, Object>) map2.get("data");
        List<String> vertices2 = (List<String>) data2.get("vertices");
        List<List<String>> edges2 = (List<List<String>>) data2.get("edges");
        int targetK2 = (int) data2.get("targetCliqueSize");

        assertEquals(6, vertices2.size());
        assertEquals(2, targetK2);

        // Stage 3: CLIQUE -> INDEPENDENT SET
        CliqueToISInput isInput3 = CliqueToISInput.builder()
                .vertices(vertices2)
                .edges(edges2)
                .cliqueSize(targetK2)
                .build();

        MvcResult res3 = mockMvc.perform(post("/api/m5/clique-to-independent-set")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(isInput3)))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> map3 = objectMapper.readValue(res3.getResponse().getContentAsString(), Map.class);
        Map<String, Object> data3 = (Map<String, Object>) map3.get("data");
        List<String> vertices3 = (List<String>) data3.get("transformedVertices");
        List<List<String>> edges3 = (List<List<String>>) data3.get("transformedEdges");
        int isSize3 = (int) data3.get("independentSetSize");

        assertEquals(targetK2, isSize3);

        // Stage 4: INDEPENDENT SET -> VERTEX COVER
        ISToVCInput vcInput4 = ISToVCInput.builder()
                .vertices(vertices3)
                .edges(edges3)
                .independentSetSize(isSize3)
                .build();

        MvcResult res4 = mockMvc.perform(post("/api/m5/independent-set-to-vertex-cover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vcInput4)))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> map4 = objectMapper.readValue(res4.getResponse().getContentAsString(), Map.class);
        Map<String, Object> data4 = (Map<String, Object>) map4.get("data");
        int vcSize4 = (int) data4.get("vertexCoverSize");
        boolean verified4 = (boolean) data4.get("verified");

        assertTrue(verified4);
        assertEquals(vertices3.size(), isSize3 + vcSize4);

        // Stage 5: Vertex Cover 2-Approximation
        VertexCoverApproxInput approxInput5 = VertexCoverApproxInput.builder()
                .vertices(vertices3)
                .edges(edges3)
                .build();

        MvcResult res5 = mockMvc.perform(post("/api/m5/vertex-cover-2approx")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(approxInput5)))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> map5 = objectMapper.readValue(res5.getResponse().getContentAsString(), Map.class);
        Map<String, Object> data5 = (Map<String, Object>) map5.get("data");
        int approxCoverSize = (int) data5.get("coverSize");
        boolean verified5 = (boolean) data5.get("isVerifiedCover");

        assertTrue(verified5);
        assertTrue(approxCoverSize <= 2 * vcSize4);
    }
}
