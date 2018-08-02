package com.zj.loglib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class ZipUtils {
    public static void zipFile(String targetFileName, File source) throws IOException {
        FileInputStream fin = null;
        FileOutputStream fout = null;
        GZIPOutputStream gzout = null;

        if (!source.exists())
            return;

        try {

            fin = new FileInputStream(source);
            fout = new FileOutputStream(new File(targetFileName));
            gzout = new GZIPOutputStream(fout);
            byte[] buf = new byte[1024];
            int num;
            while ((num = fin.read(buf)) != -1) {
                gzout.write(buf, 0, num);
            }
            fin.close();
            fin = null;
            source.delete();
        } finally {
            if (gzout != null)
                gzout.close();
            if (fout != null)
                fout.close();
            if (fin != null)
                fin.close();
        }
    }

}
