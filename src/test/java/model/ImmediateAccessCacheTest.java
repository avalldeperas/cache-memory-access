package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class ImmediateAccessCacheTest {

    private static final int BLOCK_SIZE = 8;
    private static final int CACHE_LINE_SIZE = 4;

    @Test
    void init_immediateAccess_initCache_thenHasExpectedSize() {
        ImmediateAccessCache immediateAccessCache = new ImmediateAccessCache(BLOCK_SIZE, CACHE_LINE_SIZE);

        assertEquals(CACHE_LINE_SIZE, immediateAccessCache.getCurrentCache().size());
        assertEquals(BLOCK_SIZE, immediateAccessCache.getCurrentCache().get(0).size());
    }

    @ParameterizedTest
    @MethodSource("expectedImmediateAccessResults")
    void process_immediateAccess_whenPassedValues_thenHasExpectedFailures(List<Integer> input, List<Integer> expectedFailures) {
        ImmediateAccessCache immediateAccessCache = new ImmediateAccessCache(BLOCK_SIZE, CACHE_LINE_SIZE);
        immediateAccessCache.process(input);

        List<Integer> failures = immediateAccessCache.getFailures();
        assertEquals(expectedFailures, failures);
    }

    private static Stream<Arguments> expectedImmediateAccessResults() {
        return Stream.of(
                Arguments.of(asList(24, 16, 8, 25, 2, 48, 22, 53, 2, 12, 21, 32, 22, 23, 13),
                        asList(48, 22, 53, 21, 32)),
                Arguments.of(asList(28, 29, 12, 13, 3, 22, 30, 1, 43, 44, 45, 14, 46, 8, 63, 5, 39, 64, 4, 35),
                        asList(43, 14, 46, 8, 63, 39, 64, 4, 35)),
                Arguments.of(asList(0, 16, 8, 2, 24, 48, 22, 53, 23, 50),
                        asList(48, 22, 53, 23, 50)),
                Arguments.of(asList(30, 22, 28, 5, 6, 7, 8, 9, 17, 32, 4, 55, 22, 53, 39, 12, 21, 32, 22, 13),
                        asList(32, 4, 55, 22, 53, 39, 21))
        );
    }
}