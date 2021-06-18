package model;

import java.util.ArrayList;
import java.util.List;

public class AccessCache {

    protected List<Integer> failures = new ArrayList<>();
    protected List<Integer> entryValues;
    protected List<List<Integer>> currentCache;
    protected final int blockSize;
    protected final int cacheLineSize;

    protected int block;
    protected int line;
    protected int e;

    public AccessCache(int blockSize, int cacheLineSize) {
        this.blockSize = blockSize;
        this.cacheLineSize = cacheLineSize;
        initCache(blockSize, cacheLineSize);
    }

    private void initCache(int blockSize, int cacheLineSize) {
        currentCache = new ArrayList<>();
        for (int i = 0; i < cacheLineSize; i++) {
            List<Integer> lineValues = new ArrayList<>();
            int firstValue = i * blockSize;
            for (int j = 0; j < blockSize; j++) {
                lineValues.add(firstValue + j);
            }
            currentCache.add(lineValues);
        }
    }

    protected void editCache() {
        List<Integer> lineToEdit = currentCache.get(line);
        lineToEdit.clear();

        List<Integer> newLine = new ArrayList<>();
        int firstElement = block * blockSize;
        int lastElement = firstElement + blockSize;

        for (int i = firstElement; i < lastElement; i++) {
            newLine.add(i);
        }

        lineToEdit.addAll(newLine);
    }

    public void process(List<Integer> entryValues) {
        this.entryValues = entryValues;
        List<String> valueResults = new ArrayList<>();

        for(Integer value: entryValues) {
            if (hasFailure(value)) {
                handleFailure(value);
                valueResults.add(buildResultStr(value));
            }
        }

        printExecutionResults(valueResults);
    }

    protected boolean hasFailure(Integer value) {
        return false;
    }

    protected void handleFailure(Integer value) {
        failures.add(value);

        calculateFailure(value);

        editCache();
    }

    protected void calculateFailure(Integer value) {
    }

    protected void printExecutionResults(List<String> valueResults) {
        System.out.println("Lectures = " + entryValues);
        valueResults.forEach(System.out::println);
    }


    public List<String> calculateRates(double errorAccessTime, double accuracyAccessTime) {
        List<String> rateStrings = new ArrayList<>();
        double errorRate = calculateErrorRate();
        double accuracyRate = 1 - errorRate;
        rateStrings.add(String.format("Tf = %d fallades / %d accessos = %s", failures.size(), entryValues.size(), errorRate));
        rateStrings.add(String.format("Te = 1 - Tf = 1 - %s = %s", errorRate, accuracyRate));

        if (hasAccessTimes(errorAccessTime, accuracyAccessTime)) {
            double averageTime = accuracyRate * accuracyAccessTime + errorRate * errorAccessTime;
            rateStrings.add(String.format("tm = Te * te + Tf * tf = %s * %s + %s * %s = %sns",
                    accuracyRate, accuracyAccessTime, errorRate, errorAccessTime, averageTime));
        }

        return rateStrings;
    }

    protected String buildResultStr(int value) {
        return "";
    }

    private boolean hasAccessTimes(double errorAccessTime, double accuracyAccessTime) {
        return errorAccessTime != -1 && accuracyAccessTime != -1;
    }

    protected double calculateErrorRate() {
        return (double) failures.size() / entryValues.size();
    }

    protected void print(List<List<Integer>> currentCache) {
        System.out.println("Estat inicial: ");
        for (List<Integer> lineValues: currentCache) {
            Integer firstLineValue = lineValues.get(0);
            Integer lastLineValue = lineValues.get(lineValues.size() - 1);
            System.out.printf("%s - %s\n", firstLineValue, lastLineValue);
        }
        System.out.println();
    }

    public List<List<Integer>> getCurrentCache() {
        return currentCache;
    }

    public List<Integer> getFailures() {
        return failures;
    }
}
