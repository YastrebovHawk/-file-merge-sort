package com.yastrebovd.sortmerge.merge;

import java.util.ArrayList;
import java.util.List;

public class SortedMergeArraysNumber implements Sorting<Long> {
    private final boolean isRevers;

    public SortedMergeArraysNumber(boolean isRevers) {
        this.isRevers = isRevers;
    }

    @Override
    public List<Long> mergeArray(List<Long> arrayNumberA, List<Long> arrayNumberB) {
        List<Long> arrayNumberC = new ArrayList<>(arrayNumberA.size() + arrayNumberB.size());

        int positionA = 0, positionB = 0;
        if (isRevers) {
            while (positionA + positionB < arrayNumberA.size() + arrayNumberB.size()) {
                if (positionA == arrayNumberA.size()) {
                    arrayNumberC.add(positionA + positionB, arrayNumberB.get(positionB));
                    positionB++;
                } else if (positionB == arrayNumberB.size()) {
                    arrayNumberC.add(positionA + positionB, arrayNumberA.get(positionA));
                    positionA++;
                } else if (arrayNumberA.get(positionA) > arrayNumberB.get(positionB)) {
                    arrayNumberC.add(positionA + positionB, arrayNumberA.get(positionA));
                    positionA++;
                } else {
                    arrayNumberC.add(positionA + positionB, arrayNumberB.get(positionB));
                    positionB++;
                }
            }
            return arrayNumberC;
        } else {
            while (positionA + positionB < arrayNumberA.size() + arrayNumberB.size()) {
                if (positionA == arrayNumberA.size()) {
                    arrayNumberC.add(positionA + positionB, arrayNumberB.get(positionB));
                    positionB++;
                } else if (positionB == arrayNumberB.size()) {
                    arrayNumberC.add(positionA + positionB, arrayNumberA.get(positionA));
                    positionA++;
                } else if (arrayNumberA.get(positionA) < arrayNumberB.get(positionB)) {
                    arrayNumberC.add(positionA + positionB, arrayNumberA.get(positionA));
                    positionA++;
                } else {
                    arrayNumberC.add(positionA + positionB, arrayNumberB.get(positionB));
                    positionB++;
                }
            }
            return arrayNumberC;
        }
    }

    public List<Long> sortArray(List<Long> arrayA, boolean isIncrease) {
        if (arrayA == null) {
            return null;
        }
        if (arrayA.size() < 2) {
            return arrayA;
        }
        List<Long> arrayB = arrayA.subList(0, arrayA.size() / 2);
        List<Long> arrayC = arrayA.subList(arrayA.size() / 2, arrayA.size());
        arrayB = sortArray(arrayB, isIncrease);
        arrayC = sortArray(arrayC, isIncrease);
        return mergeArray(arrayB, arrayC);
    }
}
