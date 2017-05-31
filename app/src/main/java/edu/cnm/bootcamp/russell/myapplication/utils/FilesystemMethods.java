package edu.cnm.bootcamp.russell.myapplication.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by russell on 5/28/17.
 */

public class FilesystemMethods {

    public static boolean downloadFile(Context context, String url) {
        boolean success = false;
        if (url != null && url.indexOf("/") > 0) {
            String filename = url.substring(url.lastIndexOf("/") + 1);

            try {
                URL u = new URL(url);
                HttpURLConnection uc = (HttpURLConnection) u.openConnection();
                uc.setRequestProperty("Accept", "image/*");

                InputStream in = uc.getInputStream();

                if (in != null) {
                    int length = uc.getContentLength();

                    String dlpath = context.getCacheDir().getAbsolutePath();
                    File f = new File(dlpath, filename);
                    FileOutputStream outfile = new FileOutputStream(f);
                    byte[] buffer = new byte[1024];
                    int len;

                    while ((len = in.read(buffer)) != -1) {
                        outfile.write(buffer, 0, len);
                    }

                    in.close();
                    outfile.close();

                    File file = new File(dlpath, filename);
                    if (file.length() < length && file.exists()) {
                        file.delete();
                    }
                    else {
                        success = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    public static Bitmap getDownloadedImage(Context context, String url, int width) {
        Bitmap bitmap = null;

        if (url != null && url.indexOf("/") > 0) {
            String filename = url.substring(url.lastIndexOf("/") + 1);
            String dlpath = context.getCacheDir().getAbsolutePath();
            File file = new File(dlpath, filename);
            if (file.exists()) {
                FileInputStream file_stream = null;
                try {
                    file_stream = new FileInputStream(file);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }

                if (file_stream != null) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                    options.inSampleSize = calculateInSampleSize(options, width);
                    options.inJustDecodeBounds = false;
                    BufferedInputStream buffer = new BufferedInputStream(file_stream);
                    try {
                        bitmap = BitmapFactory.decodeStream(buffer, null, options);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                    try {
                        buffer.close();
                        file_stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
        int width = options.outWidth;
        int inSampleSize = 1;

        if (width > reqWidth) {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        return inSampleSize;
    }
}
