package com.example.android.notepad.util;

import java.io.File;
import android.os.Build;
import java.util.Set;
import java.util.HashSet;
import static android.os.Environment.getExternalStorageDirectory;
import  static  com.example.android.notepad.util.FileChmod.chmodPlusRWX;
import android.content.Context;
import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by dongyanyan on 16/7/26.
 */
public final class ScreenShot {

    static final String SPOON_SCREENSHOTS = "spoon-screenshots";
    static final String SPOON_FILES = "spoon-files";
    static final String NAME_SEPARATOR = "_";
    private static final String EXTENSION = ".png";
    private static final String TAG = "Spoon";
    private static final Object LOCK = new Object();


    private static Set<String> clearedOutputDirectories = new HashSet<String>();

    public static File screenshotpath(Context context,String tag,String testClassName,String testMethodName) {
        try {
            File screenshotDirectory = obtainScreenshotDirectory(context, testClassName,testMethodName);
            String screenshotName = System.currentTimeMillis() + NAME_SEPARATOR + tag + EXTENSION;
            File screenshotFile = new File(screenshotDirectory, screenshotName);
            return screenshotFile;
        } catch (Exception e) {
            throw new RuntimeException("Unable to capture screenshot.", e);
        }
    }

    private static File obtainScreenshotDirectory(Context context, String testClassName,
                                                  String testMethodName) throws IllegalAccessException {
        return filesDirectory(context, SPOON_SCREENSHOTS, testClassName, testMethodName);
    }

    private static File filesDirectory(Context context, String directoryType, String testClassName,
                                       String testMethodName) throws IllegalAccessException {
        File directory =null;
        if (Build.VERSION.SDK_INT >= 21) {
            // Use external storage.
            directory = new File(getExternalStorageDirectory(), "app_" + directoryType);
        } else {
            // Use internal storage.
            directory = context.getDir(directoryType, MODE_WORLD_READABLE);
        }

        synchronized (LOCK) {
            if (!clearedOutputDirectories.contains(directoryType)) {
                deletePath(directory, false);
                clearedOutputDirectories.add(directoryType);
            }
        }

        File dirClass = new File(directory, testClassName);
        File dirMethod = new File(dirClass, testMethodName);
        createDir(dirMethod);
        return dirMethod;
    }

    private static void createDir(File dir) throws IllegalAccessException {
        File parent = dir.getParentFile();
        if (!parent.exists()) {
            createDir(parent);
        }
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalAccessException("Unable to create output dir: " + dir.getAbsolutePath());
        }
        chmodPlusRWX(dir);
    }

    private static void deletePath(File path, boolean inclusive) {
        if (path.isDirectory()) {
            File[] children = path.listFiles();
            if (children != null) {
                for (File child : children) {
                    deletePath(child, true);
                }
            }
        }
        if (inclusive) {
            path.delete();
        }
    }
}
