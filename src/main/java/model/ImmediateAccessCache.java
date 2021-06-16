package model;

import java.util.List;

public class ImmediateAccessCache extends AccessCache {

    public ImmediateAccessCache(int blockSize, int cacheLineSize) {
        super(blockSize, cacheLineSize);
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
        line = block % cacheLineSize;
        e = block / cacheLineSize;
    }

    @Override
    public String buildResultStr(int value) {
        String failureTitle = String.format("Fallada amb %s\n", value);

        int firstNumber = block * blockSize;
        int lastNumber = firstNumber + blockSize - 1;
        String bLine = String.format("b = d div %d = %d div %d = %d --> %d * %d = %d - %d\n",
                blockSize, value, blockSize, block, blockSize, block, firstNumber, lastNumber);

        String lLine = String.format("l = b mod %d = %d mod %d = %d\n",
                cacheLineSize, block, cacheLineSize, line);

        String eLine = String.format("e = b div %d = %d div %d = %d\n",
                cacheLineSize, block, cacheLineSize, e);

        String finalLine = String.format("%d:%d (%d-%d) a la línia %d\n",
                block, e, firstNumber, lastNumber, line);

        return failureTitle + bLine + lLine + eLine + finalLine;
    }
}
