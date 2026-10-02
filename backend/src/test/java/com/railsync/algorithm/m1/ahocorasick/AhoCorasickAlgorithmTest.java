package com.railsync.algorithm.m1.ahocorasick;

import com.railsync.algorithm.m1.ahocorasick.dto.AhoCorasickInput;
import com.railsync.algorithm.m1.ahocorasick.dto.AhoCorasickResult;
import com.railsync.algorithm.m1.ahocorasick.dto.KeywordMatchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AhoCorasickAlgorithmTest {

    private AhoCorasickAlgorithm ahoCorasickAlgorithm;

    @BeforeEach
    void setUp() {
        ahoCorasickAlgorithm = new AhoCorasickAlgorithm();
    }

    // Reference implementation for test validation ONLY (Naive multi-pattern search)
    private List<String> naiveReferenceSearch(String text, List<String> keywords) {
        List<String> found = new ArrayList<>();
        if (text == null || text.isEmpty() || keywords == null) return found;

        for (int i = 0; i < text.length(); i++) {
            for (String kw : keywords) {
                if (kw == null || kw.isEmpty()) continue;
                if (i + kw.length() <= text.length()) {
                    boolean match = true;
                    for (int j = 0; j < kw.length(); j++) {
                        if (text.charAt(i + j) != kw.charAt(j)) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        found.add(kw + "@" + i);
                    }
                }
            }
        }
        return found;
    }

    @Test
    @DisplayName("Railway alert detection - synthetic alert text")
    void testRailwayAlertDetection() {
        String alertText = "NOTICE: Train 12951 delay due to platform maintenance at New Delhi. Route diverted and rescheduled.";
        List<String> alertKeywords = List.of("delay", "cancelled", "platform", "diverted", "maintenance", "rescheduled");

        AhoCorasickInput input = AhoCorasickInput.builder()
                .text(alertText)
                .keywords(alertKeywords)
                .traceEnabled(true)
                .build();

        AhoCorasickResult result = ahoCorasickAlgorithm.execute(input);

        assertThat(result.getMatchedKeywords()).contains("delay", "platform", "maintenance", "diverted", "rescheduled");
        assertThat(result.getMatchedKeywords()).doesNotContain("cancelled");
        assertThat(result.getAutomatonNodes()).isNotEmpty();
        assertThat(result.getTrace()).isNotEmpty();
    }

    @Test
    @DisplayName("Compare against reference implementation")
    void testAgainstReferenceImplementation() {
        String text = "she_sells_sea_shells_on_the_sea_shore";
        List<String> keywords = List.of("she", "he", "sea", "shore", "sells");

        AhoCorasickInput input = AhoCorasickInput.builder()
                .text(text)
                .keywords(keywords)
                .build();

        AhoCorasickResult result = ahoCorasickAlgorithm.execute(input);
        List<String> referenceResults = naiveReferenceSearch(text, keywords);

        List<String> actualResults = new ArrayList<>();
        for (KeywordMatchDto match : result.getMatches()) {
            actualResults.add(match.getKeyword() + "@" + match.getStartIndex());
        }

        assertThat(actualResults).containsExactlyInAnyOrderElementsOf(referenceResults);
    }

    @Test
    @DisplayName("Overlapping and embedded matches (e.g. 'he', 'she', 'his', 'hers')")
    void testOverlappingMatches() {
        String text = "ushers";
        List<String> keywords = List.of("he", "she", "his", "hers");

        AhoCorasickInput input = AhoCorasickInput.builder()
                .text(text)
                .keywords(keywords)
                .build();

        AhoCorasickResult result = ahoCorasickAlgorithm.execute(input);

        assertThat(result.getMatchedKeywords()).contains("she", "he", "hers");
    }

    @Test
    @DisplayName("Empty text input")
    void testEmptyTextInput() {
        AhoCorasickInput input = AhoCorasickInput.builder()
                .text("")
                .keywords(List.of("delay", "cancelled"))
                .build();

        AhoCorasickResult result = ahoCorasickAlgorithm.execute(input);

        assertThat(result.getMatches()).isEmpty();
    }

    @Test
    @DisplayName("Empty keyword list")
    void testEmptyKeywordList() {
        AhoCorasickInput input = AhoCorasickInput.builder()
                .text("Train 12951 is delayed")
                .keywords(List.of())
                .build();

        AhoCorasickResult result = ahoCorasickAlgorithm.execute(input);

        assertThat(result.getMatches()).isEmpty();
    }

    @Test
    @DisplayName("Pattern longer than text")
    void testPatternLongerThanText() {
        AhoCorasickInput input = AhoCorasickInput.builder()
                .text("SHORT")
                .keywords(List.of("VERY_LONG_RAILWAY_KEYWORD"))
                .build();

        AhoCorasickResult result = ahoCorasickAlgorithm.execute(input);

        assertThat(result.getMatches()).isEmpty();
    }

    @Test
    @DisplayName("Unicode input with emojis and station names")
    void testUnicodeInput() {
        String text = "🚨ALERT: 🚉NEW_DELHI_JN delay & 🚆MUMBAI_EXPRESS diverted";
        List<String> keywords = List.of("🚉NEW_DELHI_JN", "delay", "🚆MUMBAI_EXPRESS", "diverted");

        AhoCorasickInput input = AhoCorasickInput.builder()
                .text(text)
                .keywords(keywords)
                .build();

        AhoCorasickResult result = ahoCorasickAlgorithm.execute(input);

        assertThat(result.getMatchedKeywords()).containsAll(keywords);
    }

    @Test
    @DisplayName("Deterministic behavior - multiple runs produce exact same results")
    void testDeterministicBehavior() {
        AhoCorasickInput input = AhoCorasickInput.builder()
                .text("delay_diverted_delay_cancelled")
                .keywords(List.of("delay", "diverted", "cancelled"))
                .build();

        AhoCorasickResult r1 = ahoCorasickAlgorithm.execute(input);
        AhoCorasickResult r2 = ahoCorasickAlgorithm.execute(input);

        assertThat(r1.getMatchCount()).isEqualTo(r2.getMatchCount());
        assertThat(r1.getMatchedKeywords()).isEqualTo(r2.getMatchedKeywords());
    }
}
