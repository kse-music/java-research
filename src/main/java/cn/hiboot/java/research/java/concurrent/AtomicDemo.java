package cn.hiboot.java.research.java.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/8/9 23:45
 */
public class AtomicDemo {

    private int a =0;
    private LongAdder longAdder = new LongAdder();
    private AtomicLong atomicLong = new AtomicLong();
    private AtomicInteger atomicInteger = new AtomicInteger();

    @Test
    public void atomic(){
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    a++;
                    longAdder.increment();
                    atomicLong.getAndIncrement();
                    atomicInteger.getAndIncrement();
                }
            }).start();
        }
        try {
            Thread.sleep(3000);
            System.out.println(a);
            System.out.println(longAdder.longValue());
            System.out.println(atomicLong.longValue());
            System.out.println(atomicInteger.intValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void demo(){
        System.out.println(longAdder.longValue());
    }


}
