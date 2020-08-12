package cn.hiboot.java.research.java.java8;

@FunctionalInterface
public interface Converter<F, T> {
    T convert(F from);
}
