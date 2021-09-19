package com.yastrebovd.sortmerge.merge;

import com.yastrebovd.sortmerge.handler.Converter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class MergeSortedFilesNumber extends MergeSortedFiles<Long> {

    public MergeSortedFilesNumber(Converter<Long> converter, boolean isRevers) {
        super(converter, isRevers);
    }

    @Override
    public void mix(String nameFile1, String nameFile2, String out) {
        File file1 = new File(nameFile1);
        File file2 = new File(nameFile2);
        try (FileReader fileReader1 = new FileReader(file1);
             FileReader fileReader2 = new FileReader(file2);
             FileWriter fileWriter = new FileWriter(out);
             BufferedReader bufferedReader1 = new BufferedReader(fileReader1);
             BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            String lineFile1 = bufferedReader1.readLine();
            String lineFile2 = bufferedReader2.readLine();
            if (super.isRevers) {
                while (true) {
                    if (lineFile1 != null && lineFile2 != null) {
                        if (super.converter.transformIntoT(lineFile1) >= super.converter.transformIntoT(lineFile2)) {
                            bufferedWriter.write(lineFile1);
                            bufferedWriter.newLine();
                            lineFile1 = bufferedReader1.readLine();
                        } else {
                            bufferedWriter.write(lineFile2);
                            bufferedWriter.newLine();
                            lineFile2 = bufferedReader2.readLine();
                        }
                    } else if (lineFile1 == null) {
                        if (lineFile2 != null) {
                            bufferedWriter.write(lineFile2);
                            bufferedWriter.newLine();
                            lineFile2 = bufferedReader2.readLine();
                        } else break;
                    } else {
                        bufferedWriter.write(lineFile1);
                        bufferedWriter.newLine();
                        lineFile1 = bufferedReader1.readLine();
                    }
                }
            } else {
                while (true) {
                    if (lineFile1 != null && lineFile2 != null) {
                        if (super.converter.transformIntoT(lineFile1) <= super.converter.transformIntoT(lineFile2)) {
                            bufferedWriter.write(lineFile1);
                            bufferedWriter.newLine();
                            lineFile1 = bufferedReader1.readLine();
                        } else {
                            bufferedWriter.write(lineFile2);
                            bufferedWriter.newLine();
                            lineFile2 = bufferedReader2.readLine();
                        }
                    } else if (lineFile1 == null) {
                        if (lineFile2 != null) {
                            bufferedWriter.write(lineFile2);
                            bufferedWriter.newLine();
                            lineFile2 = bufferedReader2.readLine();
                        } else break;
                    } else {
                        bufferedWriter.write(lineFile1);
                        bufferedWriter.newLine();
                        lineFile1 = bufferedReader1.readLine();
                    }

                }
            }
            file1.delete();
            file2.delete();
        } catch (IOException ignored) {
            log.error(ignored.fillInStackTrace().toString());
        }
    }
}
