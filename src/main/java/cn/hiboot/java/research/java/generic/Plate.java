package cn.hiboot.java.research.java.generic;

/**
 * 盘子
 *
 * @author DingHao
 * @since 2018/12/14 22:18
 */
public class Plate<T> {
    private T item;

    public Plate(T t) {
        item = t;
    }

    public void set(T t) {
        item = t;
    }

    public T get() {
        return item;
    }
}
