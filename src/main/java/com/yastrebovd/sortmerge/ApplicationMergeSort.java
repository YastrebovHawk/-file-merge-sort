package com.yastrebovd.sortmerge;


import com.yastrebovd.sortmerge.thread.ApplicationMergeSortFilesRun;

import java.io.File;

public class ApplicationMergeSort {
    public static void main(String[] args)  {
        String[] s = {"-a", "-s", "out.txt",
        "in1.txt",
                "in3.txt",
                "in2.txt"};

        Command.selectCommand(s);
    }
}
