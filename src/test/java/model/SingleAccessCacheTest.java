package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SingleAccessCacheTest {

    private static final int BLOCK_SIZE = 8;
    private static final int CACHE_LINE_SIZE = 4;

    @Test
    void init_immediateAccess_initCache_thenHasExpectedSize() {
        SingleFailureAccessCache singleFailureAccessCache = new SingleFailureAccessCache(BLOCK_SIZE, CACHE_LINE_SIZE);

        assertEquals(CACHE_LINE_SIZE, singleFailureAccessCache.getCurrentCache().size());
        assertEquals(BLOCK_SIZE, singleFailureAccessCache.getCurrentCache().get(0).size());
    }

    @ParameterizedTest
    @MethodSource("expectedSingleFailureAccess")
    void process_immediateAccess_whenPassedValues_thenHasExpectedFailures(List<Integer> input, List<Integer> expectedFailures) {
        SingleFailureAccessCache singleFailureAccessCache = new SingleFailureAccessCache(BLOCK_SIZE, CACHE_LINE_SIZE);
        singleFailureAccessCache.process(input);

        List<Integer> failures = singleFailureAccessCache.getFailures();
        assertEquals(expectedFailures, failures);
    }

    private static Stream<Arguments> expectedSingleFailureAccess() {
        return Stream.of(
                Arguments.of(singletonList(24), singletonList(24),
                Arguments.of(singletonList(28), singletonList(24))
        ));
    }
}