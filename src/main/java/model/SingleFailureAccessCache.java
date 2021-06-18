package model;

import java.util.ArrayList;
import java.util.List;

public class SingleFailureAccessCache extends AccessCache {

    public SingleFailureAccessCache(int blockSize, int cacheLineSize) {
        super(blockSize, cacheLineSize);
    }

    @Override
    public void process(List<Integer> entryValues) {
        List<String> resultStrs = new ArrayList<>();
        for(Integer entryValue: entryValues) {
            calculateFailure(entryValue);
            resultStrs.add(buildResultStr(entryValue));

        }
        resultStrs.forEach(System.out::println);
    }

    @Override
    public void calculateFailure(Integer value) {
        block = value / blockSize;
        failures.add(value);
    }

    @Override
    public String buildResultStr(int value) {
        String failureTitle = String.format("Fallada amb %s\n", value);

        int firstNumber = block * blockSize;
        int lastNumber = firstNumber + blockSize - 1;
        String bLine = String.format("b = d div %d = %d div %d = %d --> %d * %d = %d - %d\n",
                blockSize, value, blockSize, block, blockSize, block, firstNumber, lastNumber);

        String finalLine = String.format("%d (%d-%d)\n",
                block, firstNumber, lastNumber);

        return failureTitle + bLine + finalLine;
    }
}
