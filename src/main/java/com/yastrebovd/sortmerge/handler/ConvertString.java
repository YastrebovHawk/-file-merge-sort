package com.yastrebovd.sortmerge.handler;

public class ConvertString implements Converter<String> {
    @Override
    public String transformIntoT(String line) {
        if (line == null){
            throw new  NullPointerException();
        }
        return line;
    }

    @Override
    public String transformIntoString(String line) {
        if (line == null){
            throw new NullPointerException();
        }
        return line;
    }
}
