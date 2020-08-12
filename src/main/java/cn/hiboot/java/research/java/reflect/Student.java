package cn.hiboot.java.research.java.reflect;


/**
 * @author DingHao
 * @since 2018/12/8 21:33
 */
@BTable
public class Student extends Super implements AnnotationIf,Comparable<Student>{
    private int classNo;

    //---------------构造方法-------------------
    //（默认的构造方法）
    Student(String str){
        System.out.println("(默认)的构造方法 s = " + str);
    }

    //无参构造方法
    public Student(){
        System.out.println("调用了公有、无参构造方法执行了。。。");
    }

    //有一个参数的构造方法
    public Student(char name){
        System.out.println("姓名：" + name);
    }

    //有多个参数的构造方法
    public Student(String name ,int age){
        System.out.println("姓名："+name+"年龄："+ age);//这的执行效率有问题，以后解决。
    }

    //受保护的构造方法
    protected Student(boolean n){
        System.out.println("受保护的构造方法 n = " + n);
    }

    //私有构造方法
    private Student(int age){
        System.out.println("私有的构造方法   年龄："+ age);
    }

    public int getClassNo() {
        return classNo;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return true;
    }

    @Override
    public int compareTo(Student o) {
        return 0;
    }

    @Override
    public void aa() {

    }

    private class A{

    }

    public interface B{
        class D{

        }
    }

    abstract class C{

    }
}
