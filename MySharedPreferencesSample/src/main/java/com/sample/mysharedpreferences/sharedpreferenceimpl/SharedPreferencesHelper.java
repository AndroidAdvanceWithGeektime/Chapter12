package com.sample.mysharedpreferences.sharedpreferenceimpl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;

public class SharedPreferencesHelper {
    private static boolean mCanUseCustomSp = true;
    private static boolean mHasCheck = false;

    private static Method mGetSharedPrefsFileMethod;

    private static volatile ExecutorService sCachedThreadPool;

    public static synchronized boolean canUseCustomSp() {
        if (!mHasCheck) {
            mHasCheck = true;
            if (!QueuedWork.init() || !FileUtils.init() || !XmlUtils.init()) {
                mCanUseCustomSp = false;
            }
        }

        return mCanUseCustomSp;
    }

    public static File getSharedPrefsFile(Context context, String name) {
        if (mGetSharedPrefsFileMethod == null) {
            try {
                mGetSharedPrefsFileMethod = context.getClass().getMethod("getSharedPrefsFile", String.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        File prefsFile = null;
        if (mGetSharedPrefsFileMethod != null) {
            try {
                prefsFile = (File) mGetSharedPrefsFileMethod.invoke(context, name);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return prefsFile;
    }

    /**
     * 暂时只有这里需要用到cachedThreadPool。以后如果有更多业务需要用了再考虑提供统一接口。
     * 使用cachedThreadPool是为了保证任务总是立即调度而不需要等待，并减少碎片化任务频繁创建线程的耗时
     */
    static void execute(Runnable task) {
        if (sCachedThreadPool == null) {
            synchronized (SharedPreferencesHelper.class) {
                if (sCachedThreadPool == null) {
                    sCachedThreadPool = Executors.newCachedThreadPool();
                }
            }
        }
        sCachedThreadPool.execute(task);
    }
}
