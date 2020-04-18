package com.example.administrator.pushapp.cache;

import android.os.Handler;
import android.os.Looper;

import java.util.List;

/**
 * <p>@author : tangyanghai</p>
 * <p>@time : 2020/4/16</p>
 * <p>@for : </p>
 * <p></p>
 */
public class CacheUtils implements ICache {

    private static final CacheUtils instance = new CacheUtils();

    public static CacheUtils getInstance() {
        return instance;
    }

    private CacheUtils() {
    }

    @Override
    public synchronized <T> void add(final T... list) {
        new Thread() {
            @Override
            public void run() {
                NativeCache.getInstance().add(list);
                BoxCache.getInstance().add(list);
            }
        }.start();
    }

    @Override
    public synchronized <T> List<T> find(Class<T> cls) {
        List<T> ts = NativeCache.getInstance().find(cls);
        if (ts != null && ts.size() > 0) {
            return ts;
        }

        ts = BoxCache.getInstance().find(cls);
        if (ts != null && ts.size() > 0) {
            for (T t : ts) {
                NativeCache.getInstance().add(t);
            }
        }
        return ts;
    }

    public synchronized <T> void findAsyn(final Class<T> clz, final OnFindData<T> lisenter) {
        if (lisenter == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                final List<T> ts = find(clz);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        lisenter.onFind(ts);
                    }
                });
            }
        }.start();
    }
}
