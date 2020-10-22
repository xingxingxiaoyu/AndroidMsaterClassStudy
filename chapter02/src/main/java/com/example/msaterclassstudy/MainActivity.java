package com.example.msaterclassstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button_timeout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeOutBean timeOutBean = new TimeOutBean();
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: ");
                        Runtime.getRuntime().gc();
                    }
                }, 1000);
            }
        });
        findViewById(R.id.button_fix).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    final Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
                    final Field field = clazz.getDeclaredField("INSTANCE");
                    field.setAccessible(true);
                    final Object watchdog = field.get(null);

                    //可以停住FinalizerWatchdogDaemon线程，然后就不会包那个超时异常了
                    final Field thread = clazz.getSuperclass().getDeclaredField("thread");
                    thread.setAccessible(true);
                    thread.set(watchdog, null);

                    //直接掉stop,由于中断方法未加锁，可能会导致
//                    private boolean sleepForNanos(long durationNanos) {
//                        // It's important to base this on nanoTime(), not currentTimeMillis(), since
//                        // the former stops counting when the processor isn't running.
//                        long startNanos = System.nanoTime();
//                        while (true) {
//                            long elapsedNanos = System.nanoTime() - startNanos;
//                            long sleepNanos = durationNanos - elapsedNanos;
//                            if (sleepNanos <= 0) {
//                                return true;
//                            }
//                            // Ensure the nano time is always rounded up to the next whole millisecond,
//                            // ensuring the delay is >= the requested delay.
//                            long sleepMillis = (sleepNanos + NANOS_PER_MILLI - 1) / NANOS_PER_MILLI;
//                            try {
//                                Thread.sleep(sleepMillis);
//                            } catch (InterruptedException e) {
//                                if (!isRunning()) {
//                                    return false;
//                                }
//                            } catch (OutOfMemoryError ignored) {
//                                if (!isRunning()) {
//                                    return false;
//                                }
//                            }
//                        }
//                    }
                    //在这个函数直接返回，然后返回的很快的话，没有sleep足够的时间，对象没有回收完成，然后就报超时异常了
                    System.runFinalization();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}