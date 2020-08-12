package cn.hiboot.java.research.java.escape;


import java.io.IOException;
import java.util.Objects;
import java.util.Random;

/**
 * 首先逃逸分析是一种算法。
 * 通过逃逸分析算法可以分析出某一个方法中的某个对象是否会被其它方法或者线程访问到。
 * 如果分析结果显示某对象并不会被其它线程访问，则有可能在编译期间其做一些深层次的优化。
 *
 *1、全局逃逸（GlobalEscape）
 * 即一个对象的作用范围逃出了当前方法或者当前线程，有以下几种场景：
 *
 * 对象是一个静态变量
 *
 * 对象作为当前方法的返回值
 *
 * 如果复写了类的finalize方法，则此类的实例对象都是全局逃逸状态(因此为了提高性能，除非万不得已，不要轻易复写finalize方法)
 *
 * 2、参数逃逸（ArgEscape）
 * 即一个对象被作为方法参数传递或者被参数引用，但在调用过程中不会再被其它方法或者线程访问。
 *
 * 3、没有逃逸（NoEscape）
 * 即方法中的对象没有发生逃逸，这种对象Java即时编译器会做出进一步的优化。
 *
 * @author DingHao
 * @since 2020/4/26 14:41
 */
public class EscapeDemo {

    public static void main(String[] args) throws IOException  {

        Random random = new Random();
        for (int i = 0; i < 10000000; i++) {
            Escape escape1 = new Escape(random.nextInt(),random.nextInt());
            Escape escape2 = new Escape(random.nextInt(),random.nextInt());
            if(escape1.equals(escape2)){
                System.out.println("Prevent anything from begin optimized");
            }
        }
        System.in.read();
    }

    static class Escape{
        int a;
        int b;

        public Escape(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Escape escape = (Escape) o;
            return a == escape.a &&
                    b == escape.b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }

}
