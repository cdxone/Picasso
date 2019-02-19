package com.cdx.example.picasso.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Build.VERSION;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    public static final String SDK_CACHE_PATH = "aliCloud";

    public FileUtil() {
    }

    public static File getExternalStorageDirectory(Context var0, String var1) {
        File var2;
        if (isCanUseSDCard()) {
            var2 = new File(Environment.getExternalStorageDirectory(), var1);
        } else {
            var2 = new File(getCacheDir(var0), var1);
        }

        if (!var2.exists()) {
            var2.mkdirs();
        }

        return var2;
    }

    public static File getCachePath(Context var0) {
        return getExternalStoragePublicDirectory(var0, "aliCloud");
    }

    public static File getExternalStoragePublicDirectory(Context var0, String var1) {
        File var2;
        if (isCanUseSDCard()) {
            var2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), var1);
        } else {
            var2 = new File(getCacheDir(var0), var1);
        }

        if (!var2.exists()) {
            var2.mkdirs();
        }

        return var2;
    }

    public static boolean isCanUseSDCard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static File getCacheDir(Context var0) {
        File var1;
        if (isCanUseSDCard()) {
            if (VERSION.SDK_INT >= 8) {
                var1 = var0.getExternalCacheDir();
            } else {
                var1 = a(var0);
            }
        } else {
            var1 = var0.getCacheDir();
        }

        if (var1 != null && !var1.exists()) {
            var1.mkdirs();
        }

        return var1;
    }

    public static File getExternalCacheDir(Context var0) {
        File var1 = null;
        if (isCanUseSDCard()) {
            if (VERSION.SDK_INT >= 8) {
                var1 = var0.getExternalCacheDir();
            } else {
                var1 = a(var0);
            }
        }

        if (var1 != null && !var1.exists()) {
            var1.mkdirs();
        }

        return var1;
    }

    private static final File a(Context var0) {
        return new File(Environment.getExternalStorageDirectory(), "/Android/data/" + var0.getApplicationInfo().packageName + "/cache/");
    }

    public static void copyStream(InputStream var0, OutputStream var1) throws IOException {
        copyStream(var0, var1, true);
    }

    public static void copyStream(InputStream var0, OutputStream var1, boolean var2) throws IOException {
        byte[] var3 = new byte[8192];

        int var4;
        while(!Thread.interrupted() && (var4 = var0.read(var3, 0, 8192)) != -1) {
            var1.write(var3, 0, var4);
        }

        var1.flush();
        if (var2) {
            var1.close();
        }

    }

    public static String generate(String var0) {
        return String.valueOf(var0.hashCode());
    }
}

