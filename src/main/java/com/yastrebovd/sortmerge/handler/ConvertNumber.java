package com.yastrebovd.sortmerge.handler;

public class ConvertNumber implements Converter<Long> {
    @Override
    public Long transformIntoT(String line) throws NumberFormatException {
        if (line == null) {
            throw new  NullPointerException();
        }
        return Long.parseLong(line);
    }

    @Override
    public String transformIntoString(Long line) {
        if (line == null){
            throw new NullPointerException();
        }
        return String.valueOf(line);
    }
}
