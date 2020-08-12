package cn.hiboot.java.research.java.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 你不能利用管道与不同的JVM中的线程通信(不同的进程)。
 * 在概念上，Java的管道不同于Unix/Linux系统中的管道。在Unix/Linux中，运行在不同地址空间的两个进程可以通过管道通信。
 * 在Java中，通信的双方应该是运行在同一进程中的不同线程。
 *
 * @author DingHao
 * @since 2020/2/1 14:13
 */
public class IODemo {

    @Test
    public void pipe() throws Exception {

        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(inputStream);

        Thread t1 = new Thread(() -> {
            try {
                outputStream.write("Hello World".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {

            }
        });

        Thread t2 = new Thread(() -> {
            int data = -1;
            try {
                while ((data = inputStream.read()) != -1){
                    System.out.println(((char) data));
                }
            } catch (IOException e) {

            }
        });

        t1.start();
        t2.start();

        TimeUnit.SECONDS.sleep(3);

    }

}
