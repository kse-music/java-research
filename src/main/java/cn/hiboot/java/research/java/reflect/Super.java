package cn.hiboot.java.research.java.reflect;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/10/23 10:05
 */
@ATable
public class Super{

    private int age;
    public String name;

    public Super() {
    }

    public Super(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }


    private class Aa{

    }

    public interface Bb{
        class Db{

        }
    }

    abstract class Cc{

    }
}
