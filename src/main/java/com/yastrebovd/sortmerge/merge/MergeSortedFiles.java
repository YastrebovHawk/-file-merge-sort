package com.yastrebovd.sortmerge.merge;

import com.yastrebovd.sortmerge.handler.Converter;

/**
 * Класс производит слияния двух отсортированных файлов с типом данных <T>
 * @param <T>
 */
public abstract class MergeSortedFiles<T> {

    public final Converter<T> converter;
    public final boolean isRevers;

    /**
     *
     * @param converter
     * @param isRevers - true - сортировать по убыванию, false -сортировать по возрастанию
     */
    public MergeSortedFiles(Converter<T> converter, boolean isRevers) {
        this.converter = converter;
        this.isRevers = isRevers;
    }

    /**
     * Производис слияние двух отсортированных файлов
     * @param fileName1
     * @param fileName2
     * @param out - названия нового файла
     */
    public abstract void mix(String fileName1, String fileName2, String out);

}
