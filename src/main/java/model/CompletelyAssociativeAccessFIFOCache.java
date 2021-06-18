package model;

import java.util.Collections;
import java.util.List;

public class CompletelyAssociativeAccessFIFOCache extends AccessCache {

    public CompletelyAssociativeAccessFIFOCache(int blockSize, int cacheLineSize) {
        super(blockSize, cacheLineSize);
        overrideInitCache();
        this.line = -1;
    }

    private void overrideInitCache() {
        for (List<Integer> integers : currentCache) {
            Collections.fill(integers,-1);
        }
    }

    @Override
    public boolean hasFailure(Integer value) {
        boolean isValueFound = currentCache.stream()
                .flatMap(List::stream)
                .anyMatch(values -> values.equals(value));


        return !isValueFound;
    }

    @Override
    public void calculateFailure(Integer value) {
        block = value / blockSize;

        this.line = this.line == -1 ? 0 : ++line;

        if (this.line >= currentCache.size()) {
            this.line = 0;
        }
    }

    @Override
    public String buildResultStr(int value) {
        String failureTitle = String.format("Fallada amb %s\n", value);

        int firstNumber = block * blockSize;
        int lastNumber = firstNumber + blockSize - 1;
        String bLine = String.format("b = d div %d = %d div %d = %d --> %d * %d = %d - %d\n",
                blockSize, value, blockSize, block, blockSize, block, firstNumber, lastNumber);

        String finalLine = String.format("%d (%d-%d) a la linia %d\n",
                block, firstNumber, lastNumber, line);

        return failureTitle + bLine + finalLine;
    }

}
