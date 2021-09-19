package com.yastrebovd.sortmerge.handler;

public class CheckingString implements Checking<String> {

    @Override
    public boolean check(String line) {
        if (line == null){
            throw new NullPointerException();
        }
        return line.matches("^[^ ]+$");
    }
}
