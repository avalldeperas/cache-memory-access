import model.CompletelyAssociativeAccessLRUCache;
import model.ImmediateAccessCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Testing {

    private static final int BLOCK_SIZE = 8;
    private static final int CACHE_LINE_SIZE = 4;


    /*** EXAMEN ***/
    @Test
    void process_immediateAccess() {
        List<Integer> values = asList(7, 8, 9, 12, 4, 24, 18, 29, 15, 45, 51, 13, 73, 14, 52, 42, 28, 58, 20, 21);
        ImmediateAccessCache immediateAccessCache = new ImmediateAccessCache(BLOCK_SIZE, CACHE_LINE_SIZE);
        immediateAccessCache.process(values);

        List<Integer> failures = immediateAccessCache.getFailures();
        System.out.println("Fallades = " + failures);
        double result = (double) failures.size() / values.size();
        String rateOfErrors = String.format("tf = %d fallades / %d accessos = %s", failures.size(), values.size(), result);
        String resultStr = String.format("tf = %d fallades / %d accessos = %s", failures.size(), values.size(), result);
        System.out.println(resultStr);
    }

    @Test
    void process_completelyAssociativeLRU() {
        List<Integer> values = asList(7, 8, 9, 12, 4, 24, 18, 29, 15, 45, 51, 13, 73, 14, 52, 42, 28, 58, 20, 21);
        System.out.println("Lectures = " + values);
        CompletelyAssociativeAccessLRUCache completelyAssociative = new CompletelyAssociativeAccessLRUCache(BLOCK_SIZE, CACHE_LINE_SIZE);
        completelyAssociative.process(values);

        List<Integer> failures = completelyAssociative.getFailures();
        System.out.println("Fallades = " + failures);
        double result = (double) failures.size() / values.size();
        String resultStr = String.format("tf = %d fallades / %d accessos = %s", failures.size(), values.size(), String.valueOf(result));
        System.out.println(resultStr);
    }


    /*************IMMEDIATE ACCESS***********/

    @ParameterizedTest
    @MethodSource("expectedImmediateAccessResults")
    void process_immediateAccess_whenPassedValues_thenHasExpectedFailures(List<Integer> input, List<Integer> expectedFailures) {
        ImmediateAccessCache immediateAccessCache = new ImmediateAccessCache(BLOCK_SIZE, CACHE_LINE_SIZE);
        immediateAccessCache.process(input);

        List<Integer> failures = immediateAccessCache.getFailures();
        assertEquals(expectedFailures, failures);

        immediateAccessCache.calculateRates(0.2, 0.5);
    }


    @Test
    void init_immediateAccess_initCache_thenHasExpectedSize() {
        ImmediateAccessCache immediateAccessCache = new ImmediateAccessCache(BLOCK_SIZE, CACHE_LINE_SIZE);

        assertEquals(CACHE_LINE_SIZE, immediateAccessCache.getCurrentCache().size());
        assertEquals(BLOCK_SIZE, immediateAccessCache.getCurrentCache().get(0).size());
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

    /*************COMPLETE ASSOCIATIVE LRU ***********/

    @Test
    void init_completelyAssociative_thenHasExpectedSize() {
        CompletelyAssociativeAccessLRUCache completelyAssociative = new CompletelyAssociativeAccessLRUCache(BLOCK_SIZE, CACHE_LINE_SIZE);

        assertEquals(CACHE_LINE_SIZE, completelyAssociative.getCurrentCache().size());
        assertEquals(BLOCK_SIZE, completelyAssociative.getCurrentCache().get(0).size());
    }

    @ParameterizedTest
    @MethodSource("expectedCompletelyAssociativeLRUResults")
    void process_completelyAssociative_whenPassedValues_thenHasExpectedFailures(List<Integer> input, List<Integer> expectedFailures) {
        CompletelyAssociativeAccessLRUCache completelyAssociative = new CompletelyAssociativeAccessLRUCache(BLOCK_SIZE, CACHE_LINE_SIZE);
        completelyAssociative.process(input);

        List<Integer> failures = completelyAssociative.getFailures();
        assertEquals(expectedFailures, failures, "They are not equal.");
    }

    private static Stream<Arguments> expectedCompletelyAssociativeLRUResults() {
        return Stream.of(
                Arguments.of(asList(28, 29, 12, 13, 3, 22, 30, 1, 43, 44, 45, 14, 46, 8, 63, 5, 39, 64, 4, 35),
                        asList(43, 14, 63, 39, 64)),
                Arguments.of(asList(30, 22, 28, 5, 6, 7, 8, 9, 17, 32, 4, 55, 22, 53, 39, 12, 21, 32, 22, 13),
                        asList(32, 55, 12)),
                Arguments.of(asList(0, 16, 8, 2, 24, 48, 22, 53, 23, 50),
                                        asList(48, 22))

        );
    }
}
