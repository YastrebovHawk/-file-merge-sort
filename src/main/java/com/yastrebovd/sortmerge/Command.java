package com.yastrebovd.sortmerge;

import com.yastrebovd.sortmerge.handler.*;
import com.yastrebovd.sortmerge.merge.*;
import com.yastrebovd.sortmerge.thread.ApplicationMergeSortFilesRun;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Arrays;

/**
 * Класс, который отвечает за запуск основного класса ApplicationMergeSortFilesRun, с параметрами, который указали в консоле
 */
@Slf4j
public enum Command {
    STRING_REVERS_ORDER("-d -s") {
        @Override
        void processAction(File[] files, String out) {
            Converter<String> converter = new ConvertString();
            HandlerType<String> handlerType = new HandlerType<>(converter, new CheckingString());
            Sorting<String> sorting = new SortedMergeArraysString(true);
            MergeSortedFiles<String> mergeSortedFiles = new MergeSortedFilesString(converter, true);
            ApplicationMergeSortFilesRun<String> app = new ApplicationMergeSortFilesRun<String>(
                    handlerType, mergeSortedFiles, sorting);
            app.runExecutorServiceSortSplitterFiles(files, out);
        }
    },
    STRING_INCREASE_ORDER("-a -s") {
        @Override
        void processAction(File[] files, String out) {
            Converter<String> converter = new ConvertString();
            HandlerType<String> handlerType = new HandlerType<>(converter, new CheckingString());
            Sorting<String> sorting = new SortedMergeArraysString(false);
            MergeSortedFiles<String> mergeSortedFiles = new MergeSortedFilesString(converter, false);
            ApplicationMergeSortFilesRun<String> app = new ApplicationMergeSortFilesRun<String>(
                    handlerType, mergeSortedFiles, sorting);
            app.runExecutorServiceSortSplitterFiles(files, out);
        }
    },
    NUMBER_INCREASE_ORDER("-a -i") {
        @Override
        void processAction(File[] files, String out) {
            Converter<Long> converter = new ConvertNumber();
            HandlerType<Long> handlerType = new HandlerType<>(converter, new CheckingNumber());
            Sorting<Long> sorting = new SortedMergeArraysNumber(false);
            MergeSortedFiles<Long> mergeSortedFiles = new MergeSortedFilesNumber(converter, false);
            ApplicationMergeSortFilesRun<Long> app = new ApplicationMergeSortFilesRun<Long>(
                    handlerType, mergeSortedFiles, sorting);
            app.runExecutorServiceSortSplitterFiles(files, out);
        }
    },
    NUMBER_REVERS_ORDER("-d -i") {
        @Override
        void processAction(File[] files, String out) {
            Converter<Long> converter = new ConvertNumber();
            HandlerType<Long> handlerType = new HandlerType<>(converter, new CheckingNumber());
            Sorting<Long> sorting = new SortedMergeArraysNumber(true);
            MergeSortedFiles<Long> mergeSortedFiles = new MergeSortedFilesNumber(converter, true);
            ApplicationMergeSortFilesRun<Long> app = new ApplicationMergeSortFilesRun<Long>(
                    handlerType, mergeSortedFiles, sorting);
            app.runExecutorServiceSortSplitterFiles(files, out);
        }
    };

    private final String parameter;

    Command(String parameter) {
        this.parameter = parameter;
    }

    abstract void processAction(File[] files, String out);
    static void selectCommand(String... args){
        if (args.length < 3){
            log.error("Команды: {}\n не существует", Arrays.toString(args));
        }
        else {
            File[] files;
            if (args[0].equalsIgnoreCase("-i") || args[0].equalsIgnoreCase("-s")) {
                if (args[0].equalsIgnoreCase("-i")) {
                    files = HandlerFile.checks(Arrays.copyOfRange(args, 2, args.length));
                    if (files != null) {
                        Command.NUMBER_INCREASE_ORDER.processAction(files, args[1]);
                    }
                } else {
                    files = HandlerFile.checks(Arrays.copyOfRange(args, 2, args.length));
                    if (files != null) {
                        Command.STRING_INCREASE_ORDER.processAction(files, args[1]);
                    }
                }

            } else if ((args[0] + " " + args[1]).equalsIgnoreCase("-d -s")) {
                files = HandlerFile.checks(Arrays.copyOfRange(args, 3, args.length));
                if (files != null) {
                    Command.STRING_REVERS_ORDER.processAction(files, args[2]);
                }
            } else if ((args[0] + " " + args[1]).equalsIgnoreCase("-d -i")) {
                files = HandlerFile.checks(Arrays.copyOfRange(args, 3, args.length));
                if (files != null) {
                    Command.NUMBER_REVERS_ORDER.processAction(files, args[2]);
                }
            } else if ((args[0] + " " + args[1]).equalsIgnoreCase("-a -s")) {
                files = HandlerFile.checks(Arrays.copyOfRange(args, 3, args.length));
                if (files != null) {
                    Command.STRING_INCREASE_ORDER.processAction(files, args[2]);
                }
            } else if ((args[0] + " " + args[1]).equalsIgnoreCase("-a -i")) {
                files = HandlerFile.checks(Arrays.copyOfRange(args, 3, args.length));
                if (files != null) {
                    Command.NUMBER_INCREASE_ORDER.processAction(files, args[2]);
                }
            } else {
                log.error("Команды: {}\n не существует", Arrays.toString(args));
            }
        }
    }

    public String getParameter() {
        return parameter;
    }
}
