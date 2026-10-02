package com.railsync.document.controller;

import com.railsync.common.api.ApiResponse;
import com.railsync.document.domain.RailwayDocument;
import com.railsync.document.service.RailwayDocumentService;
import com.railsync.document.service.RailwayDocumentService.DocumentSearchResult;
import com.railsync.document.service.RailwayDocumentService.RepeatedSubstringAnalysis;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RailwayDocumentController {

    private final RailwayDocumentService documentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RailwayDocument>>> getAllDocuments() {
        List<RailwayDocument> docs = documentService.getAllDocuments();
        return ResponseEntity.ok(ApiResponse.success(docs, Map.of("count", docs.size())));
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<DocumentSearchResult>>> searchSubstring(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        List<DocumentSearchResult> results = documentService.searchSubstringAcrossDocuments(query);
        long foundCount = results.stream().filter(DocumentSearchResult::isFound).count();
        return ResponseEntity.ok(ApiResponse.success(results, Map.of("query", query != null ? query : "", "matchedDocsCount", foundCount)));
    }

    @PostMapping("/repeated-substrings")
    public ResponseEntity<ApiResponse<RepeatedSubstringAnalysis>> analyzeRepeatedSubstrings() {
        RepeatedSubstringAnalysis analysis = documentService.analyzeRepeatedSubstrings();
        return ResponseEntity.ok(ApiResponse.success(analysis, Map.of("executionTimeNanos", analysis.getExecutionTimeNanos())));
    }
}
