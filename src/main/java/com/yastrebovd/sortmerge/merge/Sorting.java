package com.yastrebovd.sortmerge.merge;

import java.util.List;

/**
 * Сортирует массив и проводит слияние двух массивов указаного типа <T>
 * @param <T>
 */
public interface Sorting<T> {
    /**
     * Сортируем по убыванию/возрастанию массив типа <T>
     * @param arrayA
     * @param isIncrease - true - сортировать по убыванию, false -сортировать по возрастанию
     * @return
     */
    List<T> sortArray(List<T> arrayA, boolean isIncrease);

    /**
     * Производим слияние двух отсортированных массивов типа <T>
     * @param arrayA
     * @param arrayB
     * @return
     */
    List<T> mergeArray(List<T> arrayA, List<T> arrayB);
}
