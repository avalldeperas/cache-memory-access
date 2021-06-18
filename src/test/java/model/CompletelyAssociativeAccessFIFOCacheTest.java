package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class CompletelyAssociativeAccessFIFOCacheTest {
    private static final int BLOCK_SIZE = 8;
    private static final int CACHE_LINE_SIZE = 4;

    @Test
    void init_completelyAssociative_thenHasExpectedSize() {
        CompletelyAssociativeAccessFIFOCache fifoAccess = new CompletelyAssociativeAccessFIFOCache(BLOCK_SIZE, CACHE_LINE_SIZE);

        assertEquals(CACHE_LINE_SIZE, fifoAccess.getCurrentCache().size());
        assertEquals(BLOCK_SIZE, fifoAccess.getCurrentCache().get(0).size());
    }

    @ParameterizedTest
    @MethodSource("expectedCompletelyAssociativeFIFOResults")
    void process_completelyAssociative_whenPassedValues_thenHasExpectedFailures(List<Integer> input, List<Integer> expectedFailures) {
        CompletelyAssociativeAccessFIFOCache fifoAccess = new CompletelyAssociativeAccessFIFOCache(BLOCK_SIZE, CACHE_LINE_SIZE);
        fifoAccess.process(input);

        List<Integer> failures = fifoAccess.getFailures();
        assertEquals(expectedFailures, failures, "They are not equal.");
    }

    private static Stream<Arguments> expectedCompletelyAssociativeFIFOResults() {
        return Stream.of(
                Arguments.of(asList(7, 8, 24, 23, 38, 39, 40, 45, 46, 47, 48, 16, 18, 20, 32, 40, 48, 33, 41, 49),
                        asList(7, 8, 24, 23, 38, 40,48))
        );
    }
}