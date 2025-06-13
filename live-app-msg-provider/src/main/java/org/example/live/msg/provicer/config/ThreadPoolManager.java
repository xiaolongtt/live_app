package org.example.live.msg.provicer.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author xiaolong
 * @Date 2025/2/8 20:19
 * @注释 线程池的配置类，管理线程池
 */
public class ThreadPoolManager {
    public static ThreadPoolExecutor commonAsyncThreadPool=new ThreadPoolExecutor(2, 8, 3, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread=new Thread(r);
            thread.setName("msg-send-thread");
            return thread;
        }
    });
}
