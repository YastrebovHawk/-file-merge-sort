package com.yastrebovd.sortmerge.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
public class HandlerFile {

    public static File[] checks(String[] nameFiles){
        int count = 0;
        File[] files = new File[nameFiles.length];
        for (String nameFile : nameFiles) {
            File file = new File(nameFile);
            if (file.isFile()){
                files[count] = file;
                count++;
            }
            else {
                log.error("Файла {} не существует, возникло ошибка {}", nameFile, new FileNotFoundException());
            }
        }
        if (files.length == 0){
            log.error("Файлов не существует:{} \n Проверьте указанные дириктории файлов", Arrays.toString(nameFiles));
            return null;
        }
        if (files.length != count){
            files = Arrays.copyOf(files, count);
        }
        return files;
    }
}
