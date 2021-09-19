package com.yastrebovd.sortmerge.merge;

import java.util.ArrayList;
import java.util.List;

public class SortedMergeArraysString implements Sorting<String> {

    private final boolean isRevers;

    public SortedMergeArraysString(boolean isRevers) {
        this.isRevers = isRevers;
    }

    public List<String> mergeArray(List<String> arrayStringA, List<String> arrayStringB) {
        List<String> arrayStringC = new ArrayList<>(arrayStringA.size() + arrayStringB.size());
        int positionA = 0, positionB = 0;
        if (isRevers) {
            while (positionA + positionB < arrayStringA.size() + arrayStringB.size()) {
                if (positionA == arrayStringA.size()) {
                    arrayStringC.add(positionA + positionB, arrayStringB.get(positionB));
                    positionB++;
                } else if (positionB == arrayStringB.size()) {
                    arrayStringC.add(positionA + positionB, arrayStringA.get(positionA));
                    positionA++;
                } else if (arrayStringA.get(positionA).length() > arrayStringB.get(positionB).length()) {
                    arrayStringC.add(positionA + positionB, arrayStringA.get(positionA));
                    positionA++;
                } else {
                    arrayStringC.add(positionA + positionB, arrayStringB.get(positionB));
                    positionB++;
                }
            }
            return arrayStringC;
        } else {
            while (positionA + positionB < arrayStringA.size() + arrayStringB.size()) {
                if (positionA == arrayStringA.size()) {
                    arrayStringC.add(positionA + positionB, arrayStringB.get(positionB));
                    positionB++;
                } else if (positionB == arrayStringB.size()) {
                    arrayStringC.add(positionA + positionB, arrayStringA.get(positionA));
                    positionA++;
                } else if (arrayStringA.get(positionA).length() < arrayStringB.get(positionB).length()) {
                    arrayStringC.add(positionA + positionB, arrayStringA.get(positionA));
                    positionA++;
                } else {
                    arrayStringC.add(positionA + positionB, arrayStringB.get(positionB));
                    positionB++;
                }
            }
            return arrayStringC;
        }
    }

    public List<String> sortArray(List<String> arrayA, boolean isIncrease) {
        if (arrayA == null) {
            return null;
        }
        if (arrayA.size() < 2) {
            return arrayA;
        }
        List<String> arrayB = arrayA.subList(0, arrayA.size() / 2);
        List<String> arrayC = arrayA.subList(arrayA.size() / 2, arrayA.size());
        arrayB = sortArray(arrayB, isIncrease);
        arrayC = sortArray(arrayC, isIncrease);
        return mergeArray(arrayB, arrayC);
    }

}
