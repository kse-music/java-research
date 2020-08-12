package cn.hiboot.java.research.java.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadPoolDemo {

    @Test
    public void pool() {

        BlockingQueue queue = new LinkedBlockingDeque<>(2);

        CustomThreadFactory f1 = new CustomThreadFactory("第一机房");
        CustomThreadFactory f2 = new CustomThreadFactory("第二机房");

        CustomRejectHandler handler = new CustomRejectHandler();

        ThreadPoolExecutor threadPoolFirst = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS, queue, f1, handler);
        ThreadPoolExecutor threadPoolSecond = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS, queue, f2, handler);

        Runnable task = new Task();

        for (int i = 0; i < 200; i++) {
            threadPoolFirst.execute(task);
            threadPoolSecond.execute(task);
        }

    }

    static class CustomRejectHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("task rejected. " + executor);
        }

    }

    static class CustomThreadFactory implements ThreadFactory {

        private final String namePrefix;
        private final AtomicInteger nextId = new AtomicInteger(1);

        public CustomThreadFactory(String namePrefix) {
            this.namePrefix = "CustomThreadFactory " + namePrefix + "-Worker-";
        }

        @Override
        public Thread newThread(Runnable r) {
            String name = namePrefix + nextId.getAndDecrement();
            Thread thread = new Thread(r, name);
            System.out.println(thread.getName());
            return thread;
        }
    }

    static class Task implements Runnable {

        private final AtomicLong count = new AtomicLong(0L);

        @Override
        public void run() {
            System.out.println("running_" + count.getAndDecrement());
        }

    }

}

