package com.railsync.document.service;

import com.railsync.algorithm.m2.kasai.KasaiAlgorithm;
import com.railsync.algorithm.m2.kasai.dto.KasaiInput;
import com.railsync.algorithm.m2.kasai.dto.KasaiResult;
import com.railsync.algorithm.m2.suffixautomaton.SuffixAutomatonAlgorithm;
import com.railsync.algorithm.m2.suffixautomaton.dto.SuffixAutomatonInput;
import com.railsync.algorithm.m2.suffixautomaton.dto.SuffixAutomatonResult;
import com.railsync.document.domain.RailwayDocument;
import com.railsync.document.repository.RailwayDocumentRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RailwayDocumentService {

    private final RailwayDocumentRepository repository;
    private final SuffixAutomatonAlgorithm suffixAutomatonAlgorithm;
    private final KasaiAlgorithm kasaiAlgorithm;

    @Getter
    @Builder
    public static class DocumentSearchResult {
        private String docIdentifier;
        private String title;
        private String docType;
        private boolean found;
        private int matchIndex;
        private String matchedSnippet;
    }

    @Getter
    @Builder
    public static class RepeatedSubstringAnalysis {
        private String combinedCorpus;
        private int maxLcpValue;
        private String longestRepeatedSubstring;
        private long executionTimeNanos;
    }

    public List<RailwayDocument> getAllDocuments() {
        return repository.findAll();
    }

    public List<DocumentSearchResult> searchSubstringAcrossDocuments(String querySubstring) {
        List<RailwayDocument> docs = repository.findAll();
        List<DocumentSearchResult> results = new ArrayList<>();

        for (RailwayDocument doc : docs) {
            SuffixAutomatonResult samResult = suffixAutomatonAlgorithm.execute(
                    SuffixAutomatonInput.builder()
                            .text(doc.getContent())
                            .query(querySubstring)
                            .build()
            );

            boolean found = samResult.isSubstringFound();
            int idx = samResult.getFirstOccurrenceIndex();
            String snippet = found && idx >= 0
                    ? doc.getContent().substring(Math.max(0, idx - 10), Math.min(doc.getContent().length(), idx + querySubstring.length() + 15))
                    : null;

            results.add(DocumentSearchResult.builder()
                    .docIdentifier(doc.getDocIdentifier())
                    .title(doc.getTitle())
                    .docType(doc.getDocType())
                    .found(found)
                    .matchIndex(idx)
                    .matchedSnippet(snippet)
                    .build());
        }

        return results;
    }

    public RepeatedSubstringAnalysis analyzeRepeatedSubstrings() {
        List<RailwayDocument> docs = repository.findAll();
        StringBuilder corpus = new StringBuilder();
        for (RailwayDocument doc : docs) {
            corpus.append(doc.getContent()).append(" # ");
        }

        String fullCorpus = corpus.toString();
        KasaiResult kasaiResult = kasaiAlgorithm.execute(KasaiInput.builder().text(fullCorpus).build());

        return RepeatedSubstringAnalysis.builder()
                .combinedCorpus(fullCorpus)
                .maxLcpValue(kasaiResult.getMaxLcpValue())
                .longestRepeatedSubstring(kasaiResult.getLongestRepeatedSubstring())
                .executionTimeNanos(kasaiResult.getExecutionTimeNanos())
                .build();
    }
}
