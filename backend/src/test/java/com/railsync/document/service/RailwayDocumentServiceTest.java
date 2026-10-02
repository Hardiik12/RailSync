package com.railsync.document.service;

import com.railsync.algorithm.m2.kasai.KasaiAlgorithm;
import com.railsync.algorithm.m2.suffixautomaton.SuffixAutomatonAlgorithm;
import com.railsync.document.domain.RailwayDocument;
import com.railsync.document.repository.RailwayDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class RailwayDocumentServiceTest {

    private RailwayDocumentRepository repository;
    private RailwayDocumentService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(RailwayDocumentRepository.class);
        SuffixAutomatonAlgorithm samAlgorithm = new SuffixAutomatonAlgorithm();
        KasaiAlgorithm kasaiAlgorithm = new KasaiAlgorithm();
        service = new RailwayDocumentService(repository, samAlgorithm, kasaiAlgorithm);
    }

    @Test
    @DisplayName("Search substring across documents using SAM adapter")
    void testSearchSubstringAcrossDocuments() {
        RailwayDocument d1 = RailwayDocument.builder()
                .docIdentifier("DOC-001")
                .title("Timetable")
                .docType("TIMETABLE")
                .content("Train 12951 Express departure New Delhi")
                .status("ACTIVE")
                .build();

        RailwayDocument d2 = RailwayDocument.builder()
                .docIdentifier("DOC-002")
                .title("Maintenance Notice")
                .docType("MAINTENANCE")
                .content("Platform 3 track maintenance scheduled")
                .status("ACTIVE")
                .build();

        given(repository.findAll()).willReturn(List.of(d1, d2));

        List<RailwayDocumentService.DocumentSearchResult> results = service.searchSubstringAcrossDocuments("Express");

        assertThat(results).hasSize(2);
        assertThat(results.get(0).isFound()).isTrue();
        assertThat(results.get(1).isFound()).isFalse();
    }
}
