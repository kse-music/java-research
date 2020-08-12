package cn.hiboot.java.research.java.gc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/10/13 19:47
 */
public class BTraceDemo {
    int add(int a,int b){
        return a + b;
    }

    public static void main(String[] args) throws IOException {
        BTraceDemo bTraceDemo = new BTraceDemo();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        for (int i = 0; i < 10; i++) {
            reader.readLine();
            int a = (int)Math.round(Math.random() * 1000);
            int b = (int)Math.round(Math.random() * 1000);
            System.out.println(bTraceDemo.add(a,b));
        }
    }
}
