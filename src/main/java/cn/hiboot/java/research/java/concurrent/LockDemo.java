package cn.hiboot.java.research.java.concurrent;


import org.junit.jupiter.api.Test;

/**
 * 偏向锁是指一段同步代码一直被一个线程所访问，那么该线程会自动获取锁。降低获取锁的代价。如果在运行过程中，同步锁只有一个线程访问，不存在多线程争用的情况，则线程是不需要触发同步的。撤销会导致STW，在锁无竞争的情况下使用，一旦有了竞争就升级为轻量级锁
 * 轻量级锁是指当锁是偏向锁的时候，被另一个线程所访问，偏向锁就会升级为轻量级锁，其他线程会通过自旋的形式尝试获取锁，不会阻塞，提高性能。
 * 重量级锁是指当锁为轻量级锁的时候，另一个线程虽然是自旋，但自旋不会一直持续下去，当自旋一定次数的时候，还没有获取到锁，就会进入阻塞，该锁膨胀为重量级锁。重量级锁会让其他申请的线程进入阻塞，性能降低。
 *
 * 自旋锁：好处是减少线程上下文切换的消耗，缺点是循环会消耗CPU。
 * 1.如果平均负载小于CPUs则一直自旋
 * 2.如果有超过(CPUs/2)个线程正在自旋，则后来线程直接阻塞
 * 3.如果正在自旋的线程发现Owner发生了变化则延迟自旋时间（自旋计数）或进入阻塞
 * 4.如果CPU处于节电模式则停止自旋
 * 5.自旋时间的最坏情况是CPU的存储延迟（CPU A存储了一个数据，到CPU B得知这个数据直接的时间差）
 * 6.自旋时会适当放弃线程优先级之间的差异
 *
 * java线程阻塞的代价：
 * java的线程是映射到操作系统原生线程之上的，如果要阻塞或唤醒一个线程就需要操作系统介入，需要在户态与核心态之间切换，
 * 这种切换会消耗大量的系统资源，因为用户态与内核态都有各自专用的内存空间，专用的寄存器等，用户态切换至内核态需要传递给许多变量、参数给内核，
 * 内核也需要保护好用户态在切换时的一些寄存器值、变量等，以便内核态调用结束后切换回用户态继续工作。
 *
 *
 * markword
 *
 状态	标志位	存储内容
 未锁定	01	对象哈希码、对象分代年龄
 轻量级锁定	00	指向锁记录的指针
 膨胀(重量级锁定)	10	执行重量级锁定的指针
 GC标记	11	空(不需要记录信息)
 可偏向	01	偏向线程ID、偏向时间戳、对象分代年龄
 *
 */
public class LockDemo {

    public static String obj1 = "obj1";
    public static String obj2 = "obj2";

    //死锁demo
    @Test
    public void dead() {
        Thread a = new Thread(new Lock1());
        Thread b = new Thread(new Lock2());
        a.start();
        b.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Lock1 implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("Lock1 running");
                while (true) {
                    synchronized (obj1) {
                        System.out.println("Lock1 lock obj1");
                        Thread.sleep(3000);//获取obj1后先等一会儿，让Lock2有足够的时间锁住obj2
                        synchronized (obj2) {
                            System.out.println("Lock1 lock obj2");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Lock2 implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("Lock2 running");
                while (true) {
                    synchronized (obj2) {
                        System.out.println("Lock2 lock obj2");
                        Thread.sleep(3000);
                        synchronized (obj1) {
                            System.out.println("Lock2 lock obj1");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
