package com.stationeryshop.utils;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

public class ThreadUtil<T> {
    private final ExecutorService executor;
    public ThreadUtil(int threadCount) {
        executor = Executors.newFixedThreadPool(threadCount);
    }
    public Future<T> executeTask(Callable<T> task) {
        return executor.submit(task);
    }

    public void executeTask(Runnable task){
        executor.submit(task);
    }

    public <T,U> Future<T> executeTask(Function<U,T> task,U input){
        return executor.submit(() -> task.apply(input));
    }

    public void shutdown() {
        executor.shutdown();
    }
}
