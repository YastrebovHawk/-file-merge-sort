package com.yastrebovd.sortmerge.handler;


public class CheckingNumber implements Checking<Long> {
    @Override
    public boolean check(String line) {
        if (line == null){
            throw new NullPointerException();
        }
        return true;
    }
}
