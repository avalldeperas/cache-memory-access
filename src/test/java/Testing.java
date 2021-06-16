import model.CompletelyAssociativeAccessLRUCache;
import model.ImmediateAccessCache;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;

public class Testing {

    private static final int BLOCK_SIZE = 8;
    private static final int CACHE_LINE_SIZE = 4;


    @Test
    void process_immediateAccess() {
        List<Integer> values = asList(7, 8, 9, 12, 4, 24, 18, 29, 15, 45, 51, 13, 73, 14, 52, 42, 28, 58, 20, 21);
        ImmediateAccessCache immediateAccessCache = new ImmediateAccessCache(BLOCK_SIZE, CACHE_LINE_SIZE);
        immediateAccessCache.process(values);

        List<String> ratesStrings = immediateAccessCache.calculateRates(0.1, 0.2);
        ratesStrings.forEach(System.out::println);
    }

    @Test
    void process_completelyAssociativeLRU() {
        List<Integer> values = asList(7, 8, 9, 12, 4, 24, 18, 29, 15, 45, 51, 13, 73, 14, 52, 42, 28, 58, 20, 21);
        CompletelyAssociativeAccessLRUCache completelyAssociative = new CompletelyAssociativeAccessLRUCache(BLOCK_SIZE, CACHE_LINE_SIZE);
        completelyAssociative.process(values);

        List<String> strings = completelyAssociative.calculateRates(1.2, 2.2);
        strings.forEach(System.out::println);
    }
}
