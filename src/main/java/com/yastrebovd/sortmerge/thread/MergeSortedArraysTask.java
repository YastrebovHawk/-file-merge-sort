package com.yastrebovd.sortmerge.thread;

import com.yastrebovd.sortmerge.merge.Sorting;

import java.util.List;
import java.util.concurrent.RecursiveTask;


/**
 * Класс, который осуществляет сортировку слияния двух массивов указанного типа <T>
 * @param <T>
 */
public class MergeSortedArraysTask<T> extends RecursiveTask<List<T>> {
    private static final int SORT_THRESHOLD = 25000;

    private final List<T> array;
    private final Sorting<T> sortMergeArray;

    public MergeSortedArraysTask(List<T> array, Sorting<T> sortMergeArray) {
        this.array = array;
        this.sortMergeArray = sortMergeArray;
    }

    /**
     * Разбивает на подзадачи, если длинна строки меньше значение SORT_THRESHOLD
     * @return
     */
    @Override
    protected List<T> compute() {
        if (array.size() < SORT_THRESHOLD) {
            return sortMergeArray.sortArray(array, true);
        }

        MergeSortedArraysTask<T> left = new MergeSortedArraysTask<T>(array.subList(0, array.size()/2), sortMergeArray);
        MergeSortedArraysTask<T> right = new MergeSortedArraysTask<T>(array.subList(array.size()/2, array.size()), sortMergeArray);
        left.fork();
        right.fork();
        return sortMergeArray.mergeArray(left.join(), right.join());
    }
}
