import model.CompletelyAssociativeAccessFIFOCache;
import model.CompletelyAssociativeAccessLRUCache;
import model.ImmediateAccessCache;
import model.SingleFailureAccessCache;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Testing {

    private static final int BLOCK_SIZE = 8;
    private static final int CACHE_LINE_SIZE = 4;


    @Test
    void process_immediateAccess() {
        List<Integer> values = asList(7, 8, 24, 23, 38, 39, 40, 45, 46, 47, 48, 2, 4, 6, 63, 40, 48, 56, 42, 50);
        ImmediateAccessCache immediateAccessCache = new ImmediateAccessCache(BLOCK_SIZE, CACHE_LINE_SIZE);
        immediateAccessCache.process(values);

        List<String> ratesStrings = immediateAccessCache.calculateRates(20, 4);
        ratesStrings.forEach(System.out::println);
    }

    @Test
    void process_completelyAssociativeLRU() {
        List<Integer> values = asList(30, 22, 28, 5, 6, 7, 8, 9, 17, 32, 4, 55, 22, 53, 39, 12, 21, 32, 22, 13);
        CompletelyAssociativeAccessLRUCache completelyAssociative = new CompletelyAssociativeAccessLRUCache(BLOCK_SIZE, CACHE_LINE_SIZE);
        completelyAssociative.process(values);

        List<String> strings = completelyAssociative.calculateRates(20, 2);
        strings.forEach(System.out::println);
    }

    @Test
    void process_completelyAssociativeFIFO() {
        List<Integer> values = asList(7, 8, 24, 23, 38, 39, 40, 45, 46, 47, 48, 16, 18, 20, 32, 40, 48, 33, 41, 49);
        CompletelyAssociativeAccessFIFOCache fifoAccess = new CompletelyAssociativeAccessFIFOCache(BLOCK_SIZE, CACHE_LINE_SIZE);
        fifoAccess.process(values);

        List<String> strings = fifoAccess.calculateRates(30, 5);
        strings.forEach(System.out::println);
    }

    @Test
    void process_singleFailureCacheAccess() {
        // 7, 8, 24, 23, 38, 39, 40, 45, 46, 47, 48, 16, 18, 20, 32, 40, 48, 33, 41, 49
        List<Integer> values = singletonList(48);
        SingleFailureAccessCache singleFailureAccessCache = new SingleFailureAccessCache(BLOCK_SIZE, CACHE_LINE_SIZE);
        singleFailureAccessCache.process(values);
    }
}
