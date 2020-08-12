package cn.hiboot.java.research.java.concurrent;


import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 无论从性能还是安全性上考虑，我们尽量使用并发包中提供的信号同步类，避免使用对象的wait()和notify()方式来进行同步。
 *
 * @author DingHao
 * @since 2019/1/26 22:51
 */
public class ConcurrentDemo {


    /**
     * 通过它可以实现让一组线程等待至某个状态之后再全部同时执行。
     * 叫做回环是因为当所有等待线程都被释放以后，CyclicBarrier可以被重用
     */
    @Test
    public void cyclicBarrier() {
        int N = 4;
        CyclicBarrier barrier = new CyclicBarrier(N);
        for (int i = 0; i < N; i++) {
            new Writer(barrier).start();
        }
        System.out.println(Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 基于空间信号
     */
    @Test
    public void semaphore() {

        Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i <= 5; i++) {
            new SecurityCheckThread(i, semaphore).start();
        }
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 基于执行时间的同步类
     * CountDownLatch是不能够重用的
     * 一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行.
     *
     * @throws InterruptedException
     */
    @Test
    public void countDown() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Thread thread1 = new TranslateThread("1st content", countDownLatch);
        Thread thread2 = new TranslateThread("2nd content", countDownLatch);
        Thread thread3 = new TranslateThread("3rd content", countDownLatch);
        thread1.start();
        thread2.start();
        thread3.start();
//        countDownLatch.await();
        countDownLatch.await(10, TimeUnit.SECONDS);
        System.out.println("所有线程执行完成");
    }

    static class TranslateThread extends Thread {
        private String content;
        private final CountDownLatch count;

        TranslateThread(String content, CountDownLatch count) {
            this.content = content;
            this.count = count;
        }

        @Override
        public void run() {
            if (Math.random() > 0.5) {
                throw new RuntimeException("原文存在非法字符");
            }
            System.out.println(content + " 的翻译已经完成，译文是...");
            count.countDown();
        }
    }


    static class SecurityCheckThread extends Thread {
        private int seq;
        private final Semaphore semaphore;

        SecurityCheckThread(int seq, Semaphore semaphore) {
            this.seq = seq;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("No." + seq + "乘客，正在检验中");

                if (seq % 2 == 0) {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("No." + seq + "乘客，身份可疑，不能出国！");
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
                System.out.println("No." + seq + "乘客已完成服务。");
            }

        }
    }

    static class Writer extends Thread {
        private CyclicBarrier cyclicBarrier;

        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println("线程" + threadName + "正在写入数据...");
            try {
                //以睡眠来模拟写入数据操作
                TimeUnit.SECONDS.sleep(2);
                System.out.println("线程" + threadName + "写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(threadName+"所有线程写入完毕，继续处理其他任务...");

        }
    }

}
