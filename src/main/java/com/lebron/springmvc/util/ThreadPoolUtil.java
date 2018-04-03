package com.lebron.springmvc.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {

    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void excute(Runnable command) {
        threadPool.execute(command);
    }

}
