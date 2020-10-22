package com.example.msaterclassstudy;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * display:
 *
 * @author DELL3620
 * @date 2020/10/22
 */
public class TimeOutBean {
    public static final String TAG = "TimeOutBean";

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.d(TAG, "finalize: start " + Thread.currentThread().getName());
        Thread.sleep(20000);
        Log.d(TAG, "finalize: end");

    }
}
