package cn.hiboot.java.research.java.concurrent;


import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadLocal在每个线程中对该变量会创建一个副本，即每个线程内部都会有一个该变量，
 * 且在线程内部任何地方都可以使用，线程之间互不影响，
 * 这样一来就不存在线程安全问题，也不会严重影响程序执行性能。
 * <p>
 * 在进行get之前，必须先set，否则会报空指针异常；
 * 如果想在get之前不需要调用set就能正常访问的话，必须重写initialValue()方法。
 * <p>
 * 应用场景:
 * 用来解决数据库连接、Session管理等。
 *
 * CopyValueIntoEveryThread
 */
public class ThreadLocalDemo {

    static ThreadLocal<Long> longLocal = ThreadLocal.withInitial(Thread.currentThread()::getId);
    //SimpleDateFormat是不安全的,让每个线程单独拥有这个对象
    static ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    static ThreadLocal<String> stringLocal = ThreadLocal.withInitial(Thread.currentThread()::getName);

    @Test
    public void local() {

        final ThreadLocalDemo test = new ThreadLocalDemo();

//        longLocal.set(Thread.currentThread().getId());
//        stringLocal.set(Thread.currentThread().getName());

        System.out.println(longLocal.get());
        System.out.println(stringLocal.get());

        Thread thread1 = new Thread(() -> {
            longLocal.set(Thread.currentThread().getId());
            stringLocal.set(Thread.currentThread().getName());
            System.out.println(longLocal.get());
            System.out.println(stringLocal.get());
        });
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(longLocal.get());
        System.out.println(stringLocal.get());

        //所以如果 ThreadLocal 没有被外部强引用的情况下，在垃圾回收的时候会被清理掉的，
        // 这样一来 ThreadLocalMap 中使用这个 ThreadLocal 的 key 也会被清理掉。
        // 但是，value 是强引用，不会被清理，这样一来就会出现 key 为 null 的 value。
        longLocal.remove();
        stringLocal.remove();
        //ThreadLocalMap 实现中已经考虑了这种情况，在调用 set()、get()、remove() 方法的时候，会清理掉 key 为 null 的记录。
        // 如果说会出现内存泄漏，那只有在出现了 key 为 null 的记录后，没有手动调用 remove() 方法，
        // 并且之后也不再调用 get()、set()、remove() 方法的情况下。
    }

    @Test
    public void dirtyData(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 2; i++) {
            executorService.execute(new MyThread());
        }
    }

    private static class MyThread extends Thread{

        private static boolean flag = true;

        @Override
        public void run() {
            if(flag){
                stringLocal.set(this.getName() + ", session info .");
                flag = false;
            }
            System.out.println(this.getName() + " 线程是 " + stringLocal.get());
//            stringLocal.remove();
        }

    }
}
