package zzl.utils;

import java.io.File;

public class FileUtils {
    public static boolean makeDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return file.mkdirs();
        }else
        {
            return true;
        }
    }

    public static String getExtension(String name) {
        int index = name.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return name.substring(index);
        }
    }
}
