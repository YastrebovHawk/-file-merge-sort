package com.yastrebovd.sortmerge.handler;

public class HandlerType<T> {
    private final Converter<T> converter;
    private final Checking<T> checking;

    public HandlerType(Converter<T> converter, Checking<T> checking) {
        this.converter = converter;
        this.checking = checking;
    }

    public Converter<T> getConvertType() {
        return converter;
    }

    public Checking<T> getCheckingFormat() {
        return checking;
    }

}
