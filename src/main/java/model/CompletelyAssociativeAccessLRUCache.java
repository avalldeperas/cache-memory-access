package model;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class CompletelyAssociativeAccessLRUCache extends AccessCache {

    private List<Integer> lineAccessesByPosition = asList(0,0,0,0);

    public CompletelyAssociativeAccessLRUCache(int blockSize, int cacheLineSize) {
        super(blockSize, cacheLineSize);
    }

    @Override
    public boolean hasFailure(Integer value) {
        int matchedLine = -1;
        for (int i = 0; i < currentCache.size(); i++) {
            for (int j = 0; j < currentCache.get(i).size(); j++) {
                if(currentCache.get(i).get(j).equals(value)) {
                    matchedLine = i;
                }
            }
        }

        if (matchedLine != -1) {
            updateAccesses(matchedLine);
        }

        return matchedLine == -1;
    }

    @Override
    public void calculateFailure(Integer value) {
        this.block = value / blockSize;
        Integer maxValue = Collections.max(lineAccessesByPosition);
        this.line = lineAccessesByPosition.indexOf(maxValue);
        lineAccessesByPosition.set(this.line, 0);
    }

    @Override
    public String buildResultStr(int value) {
        String failureTitle = String.format("Fallada amb %s\n", value);

        int firstNumber = block * blockSize;
        int lastNumber = firstNumber + blockSize - 1;
        String bLine = String.format("b = d div %d = %d div %d = %d --> %d * %d = %d - %d\n",
                blockSize, value, blockSize, block, blockSize, block, firstNumber, lastNumber);

        String finalLine = String.format("%d (%d-%d) a la linia %d, ja que fa més temps que no es referencia\n",
                block, firstNumber, lastNumber, line);

        return failureTitle + bLine + finalLine;
    }

    private void updateAccesses(int matchedLine) {
        for (int i = 0; i < lineAccessesByPosition.size(); i++) {
            Integer integer = lineAccessesByPosition.get(i);
            Integer newValue = i == matchedLine ? Integer.valueOf(0) : ++integer;
            lineAccessesByPosition.set(i, newValue);
        }
    }
}
