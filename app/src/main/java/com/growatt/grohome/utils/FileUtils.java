package com.growatt.grohome.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {


    public static void saveBitmap(Bitmap bm, String path, String picName) {
        try {
            File f = new File(path);
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBitmap(Bitmap bm, String path) {
        try {
            File f = new File(path);
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * @param is       需要输出的流
     * @param filePath 保存文件路径
     * @return
     */
    public static String fileOut(InputStream is, File filePath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] bytes = new byte[1024];
            int len;
            while ((len = bis.read(bytes)) != -1) {
                bos.write(bytes,0,len);
            }
            is.close();
            bis.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath.getAbsolutePath();
    }



    public static int copyStream(InputStream input, OutputStream output) throws Exception {
        byte[] buffer = new byte[1024*2];

        BufferedInputStream in = new BufferedInputStream(input, 1024*2);
        BufferedOutputStream out = new BufferedOutputStream(output, 1024*2);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, 1024*2)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), e.toString());
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), e.toString());
            }
        }
        return count;
    }

}
