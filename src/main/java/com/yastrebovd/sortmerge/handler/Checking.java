package com.yastrebovd.sortmerge.handler;

/**
 * Класс проверяет тип строки
 * @param <T>
 */
public interface Checking<T> {
    /**
     *Проверяем, можно ли конвектировать строку в укзаный тип <T>
     * @param line
     * @return
     */
    boolean check(String line);
}
