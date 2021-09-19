package com.yastrebovd.sortmerge.thread;

import com.yastrebovd.sortmerge.merge.MergeSortedFiles;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс, который считывает отсортированные файлы и производит их слияние
 * @param <T>
 */
@Slf4j
public class MergeSortedFilesTask<T> implements Runnable{
    /**
     * Блокирующий класс, у которого счетчик равен 2, если в список ConcurrentLinkedQueue кладут название файла, то он уменьшается.
     * Если из списка берут два значания, то блокировщик обновляется
     */
    private final CountDownLatch countDownLatch;
    /**
     * true - чтение файлов, указанных в параметре, еще продолжается, false - чтение файлов, указанных в параметре, завершено
     * Используется потоком MergeSortedFilesTask, чтобы отключиться
     */
    private final AtomicBoolean isActive;
    /**
     * Список названий файлов, которые были записаны из буфера, перед этим буффер был отсортирован. Список читает поток
     *  MergeSortedFilesTask, производит слияние этих файлов, название нового файла отправляет список
     */
    private final ConcurrentLinkedQueue<String> listFilesName;
    /**
     * Класс, который производит слияние двух отсортированных классов указаного типа <Т>
     */
    private final MergeSortedFiles<T> mergeSortedFiles;
    /**
     * Название файла из параметра, в который нужно записать результат
     */
    private final String out;

    public MergeSortedFilesTask(MergeSortedFiles<T> mergeSortedFiles, CountDownLatch countDownLatch, AtomicBoolean isActive, ConcurrentLinkedQueue<String> listFilesName, String out) {
        this.countDownLatch = countDownLatch;
        this.isActive = isActive;
        this.listFilesName = listFilesName;
        this.mergeSortedFiles = mergeSortedFiles;
        this.out = out;
    }

    /**
     * Как только поток, запускается. то он сразу блокируется, пока счетчик CountDownLatch не станет равен нулю.
     * После разблокировки, метод считывает два названия файлов из списка, производит слияние, уменьшает счетчик CountDownLatch на один
     * и блокируется. После разблокировки, проверяет условия isActive, если true, остается в цикле, если false, выходит из цикла и производит
     * слияние двух оставшихся циклов
     */
    @Override
    public void run() {
        try {
            String fileName1;
            String fileName2;
            countDownLatch.await();
            while (isActive.get()) {
                fileName1 = listFilesName.poll();
                fileName2 = listFilesName.poll();
               if (fileName1 != null && fileName2 != null){
                   String randomFileNameOut = Thread.currentThread().getName() + (int) (Math.random()*10000) + ".txt";
                   mergeSortedFiles.mix(fileName1, fileName2, randomFileNameOut);
                    listFilesName.add(randomFileNameOut);
                    countDownLatch.countDown();
               }
               countDownLatch.await();
            }
            fileName1 = listFilesName.poll();
            fileName2 = listFilesName.poll();
            if (fileName1 != null && fileName2 != null){
                mergeSortedFiles.mix(fileName1, fileName2, out);
            }
        }
        catch (InterruptedException ignored){
            log.error(ignored.fillInStackTrace().toString());
        }
    }
}
