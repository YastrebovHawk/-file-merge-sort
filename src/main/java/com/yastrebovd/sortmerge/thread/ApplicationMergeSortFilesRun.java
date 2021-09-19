package com.yastrebovd.sortmerge.thread;

import com.yastrebovd.sortmerge.merge.MergeSortedFiles;
import com.yastrebovd.sortmerge.handler.HandlerType;
import com.yastrebovd.sortmerge.merge.Sorting;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Основной класс, запускающий потоки
 * @param <T>
 */
@Slf4j
public class ApplicationMergeSortFilesRun<T> {
    /**
     * Класс помощник, содержащий в нутри классы Converting и Checking
     */
    private final HandlerType<T> handlerType;
    /**
     * Класс, который производит слияние двух отсортированных классов указаного типа <Т>
     */
    private final MergeSortedFiles<T> mergeSortedFiles;
    /**
     * Класс, который производит сортировку и слияние двух массивов указаного типа <Т>
     */
    private final Sorting<T> sortingArray;
    /**
     * Емкость буфера
     */
    private final int SIZE_BUFFER = 2000000;
    /**
     * Буфер, массив строк в который загружают строки из файлов
     */
    private List<T> buffer;
    /**
     * Пул потоков, с помощью которого производится сортировка буфера
     */
    private final ForkJoinPool forkJoinPool;
    /**
     * Список названий файлов, которые были записаны из буфера, перед этим буффер был отсортирован. Список читает поток
     *  MergeSortedFilesTask, производит слияние этих файлов, название нового файла отправляет список
     */
    private final ConcurrentLinkedQueue<String> listFilesName;
    /**
     * Блокирующий класс, у которого счетчик равен 2, если в список ConcurrentLinkedQueue кладут название файла, то он уменьшается.
     * Если из списка берут два значания, то блокировщик обновляется
     */
    private final CountDownLatch countDownLatch;
    /**
     * true - чтение файлов, указанных в параметре, еще продолжается, false - чтение файлов, указанных в параметре, завершено
     * Используется потоком MergeSortedFilesTask, чтобы отключиться
     */
    private final AtomicBoolean isAction;

    public ApplicationMergeSortFilesRun(HandlerType<T> handlerType, MergeSortedFiles<T> mergeSortedFiles,
                                        Sorting<T> sortingArray) {
        this.handlerType = handlerType;
        this.sortingArray = sortingArray;
        this.mergeSortedFiles = mergeSortedFiles;
        this.buffer = new ArrayList<>(SIZE_BUFFER);
        this.forkJoinPool = new ForkJoinPool();
        this.listFilesName = new ConcurrentLinkedQueue<>();
        this.countDownLatch = new CountDownLatch(2);
        this.isAction = new AtomicBoolean(true);
    }

    /**
     * Метод создает поток MergeSortedArraysTask, после считывает по строчно с файлов и одновременно проверяя содержимое строк, как только
     * count(счетчик строк) равен размеру буфера или чтение файлов закончено, метод передает MergeSortedArraysTask в пулл потоков ForkJoinPool для осортировки.
     * После сортировки происходит запись буфера в файл, название файла передается в список ConcurrentLinkedQueue. Если происходит исключение, то
     * метод записывает оставшиеся строки из буфера в файл
     * @param files - название файлов, из которых нужно прочитать строки в буфер
     * @param out - название фала, в который нужно записать отсортированный результат
     */
    public void runExecutorServiceSortSplitterFiles(File[] files, String out) {
        MergeSortedArraysTask<T> mergeSortedArraysTask;
        MergeSortedFilesTask<T> merge = new MergeSortedFilesTask<T>(mergeSortedFiles, countDownLatch, isAction, listFilesName, out);
        Thread mergeThread = new Thread(merge);
        mergeThread.start();
        int count = 0;
        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                while ((line != null)){
                    if (count <= SIZE_BUFFER) {
                        if (handlerType.getCheckingFormat().check(line)) {
                            try {
                                buffer.add(handlerType.getConvertType().transformIntoT(line));
                                count++;
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    } else {
                        String fileOutName = new Random().nextInt() + ".txt";
                        mergeSortedArraysTask = new MergeSortedArraysTask<T>(buffer, sortingArray);
                        forkJoinPool.submit(mergeSortedArraysTask);
                        try {
                            writeMergeArray(mergeSortedArraysTask.get(), count, fileOutName);
                        } catch (ExecutionException | InterruptedException e) {
                            log.error(e.fillInStackTrace().toString());
                        }
                        listFilesName.add(fileOutName);
                        countDownLatch.countDown();
                        buffer = new ArrayList<>(SIZE_BUFFER);
                        count = 0;
                    }
                    line = reader.readLine();
                }

            } catch (IOException e) {
                mergeSortedArraysTask = new MergeSortedArraysTask<>(buffer, sortingArray);
                String fileOutName = new Random().nextInt() + ".txt";
                mergeSortedArraysTask = new MergeSortedArraysTask<>(buffer, sortingArray);
                forkJoinPool.submit(mergeSortedArraysTask);
                try {
                    writeMergeArray(mergeSortedArraysTask.get(), count, fileOutName);
                } catch (ExecutionException | InterruptedException it) {
                    log.error(it.fillInStackTrace().toString());
                }
                listFilesName.add(fileOutName);
                isAction.set(false);
                countDownLatch.countDown();
                log.error("Произошла ошибка {}  часть даннчх потеряно", e.fillInStackTrace().toString());
            }
        }
        if (count < SIZE_BUFFER) {
            String fileOutName = new Random().nextInt() + ".txt";
            mergeSortedArraysTask = new MergeSortedArraysTask<>(buffer, sortingArray);
            forkJoinPool.submit(mergeSortedArraysTask);
            try {
                writeMergeArray(mergeSortedArraysTask.get(), count, fileOutName);
            } catch (ExecutionException | InterruptedException e) {
                log.error(e.fillInStackTrace().toString());
            }
            listFilesName.add(fileOutName);
            isAction.set(false);
            countDownLatch.countDown();
        }
        forkJoinPool.shutdown();
    }

    /**
     * Записывает буфер в файл
     * @param array - отсортированный массив
     * @param countLine - количество элементов в массиве
     * @param fileOutName - название файла
     */
    private void writeMergeArray(List<T> array, int countLine, String fileOutName) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileOutName), 50000)) {
            for (int j = 0; j < countLine; j++) {
                bufferedWriter.write(handlerType.getConvertType().transformIntoString(array.get(j)));
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            log.error(e.fillInStackTrace().toString());
        }
    }
}


