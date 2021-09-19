package com.yastrebovd.sortmerge.handler;

/**
 * Класс выполняет конвертацию из указанного типа в стоку и наоборот
 * @param <T>
 */
public interface Converter<T> {
    /**
     * Конвертируем в экземпляр объекта с типом <T>
     * @param line
     * @return
     */
    T transformIntoT(String line);

    /**
     * Переводим в строковое представление
     * @param line
     * @return
     */
    String transformIntoString(T line);
}
