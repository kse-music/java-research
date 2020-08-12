package cn.hiboot.java.research.java.reflect;


import org.junit.jupiter.api.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * JAVA反射机制是在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；
 * 对于任意一个对象，都能够调用它的任意一个方法和属性；
 * 这种动态获取的信息以及动态调用对象的方法的功能称为java语言的反射机制。
 * <p>
 * 1、获取Class对象的三种方式
 * 1.1 Object ——> getClass();
 * 1.2 任何数据类型（包括基本数据类型）都有一个"静态”的class属性
 * 1.3 通过Class类的静态方法：forName（String  className）(常用)
 * </p>
 *
 * @author DingHao
 * @since 2019/4/27 22:48
 */
public class ReflectDemo {

    @Test
    public void basic(){
        //第一种方式获取Class对象
        Student student = new Student();//这一new 产生一个Student对象，一个Class对象。
        Class stuClass = student.getClass();//获取Class对象
        System.out.println(stuClass.getName());

        //第二种方式获取Class对象
        Class stuClass2 = Student.class;
        System.out.println(stuClass == stuClass2);//判断第一种方式获取的Class对象和第二种方式获取的是否是同一个

        //第三种方式获取Class对象
        try {
            Class stuClass3 = Class.forName("cn.hiboot.java.reflect.Student");//注意此字符串必须是真实路径，就是带包名的类路径，包名.类名
            System.out.println(stuClass3 == stuClass2);//判断三种方式是否获取的是同一个Class对象
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void reflect() throws Exception {

        Class clazz = Student.class;
        //1.获取注解信息相关
        Annotation[] annotations = clazz.getAnnotations();//获取自身和父类上的可继承注解上的注解
        Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();//获取自身上的注解
        System.out.println(Arrays.toString(annotations));
        System.out.println(Arrays.toString(declaredAnnotations));

        //2.获取接口信息相关
        System.out.println(Arrays.toString(clazz.getInterfaces()));//获取自身上的接口
        System.out.println(Arrays.toString(clazz.getGenericInterfaces()));//获取自身上的接口(包含接口中的泛型信息)

        //3.获取class信息相关
        System.out.println(Arrays.toString(clazz.getClasses()));//返回自身和父类里成员public类和接口
        System.out.println(Arrays.toString(clazz.getDeclaredClasses()));//返回自身上里索引成员public类和接口不包括继承

        Class clazz2 = Class.forName("cn.hiboot.java.reflect.Student$B$D");
        Class<?> declaringClass = clazz2.getDeclaringClass();//返回本类的申明处。一个类可能是在另一个类内部定义的.
        Class<?> enclosingClass = clazz2.getEnclosingClass();//返回本类的立即封装类。定义此类的外部类.
        //与getDeclaringClass无二. 两者的区别在于内部匿名类

        //4.获取构造器信息相关
        System.out.println(Arrays.toString(clazz.getConstructors()));//获取自身的public构造器
        System.out.println(Arrays.toString(clazz.getDeclaredConstructors()));//获取自身所有构造器


        Constructor con = clazz.getConstructor(null);//获取公有、无参的构造方法
        //1>、因为是无参的构造方法所以类型是一个null,不写也可以：这里需要的是一个参数的类型，切记是类型
        //2>、返回的是描述这个无参构造函数的类对象。
        System.out.println("con = " + con);
        Object obj = con.newInstance();
        System.out.println("stu = " + obj);

        con = clazz.getDeclaredConstructor(int.class);//获取私有构造方法，并调用
        System.out.println("private con = " + con);
        con.setAccessible(true);//暴力访问(忽略掉访问修饰符)
        obj = con.newInstance(25);
        System.out.println("stu = " + obj);


        //5.获取字段信息相关
        System.out.println(Arrays.toString(clazz.getFields()));//获取自身和父类（含接口）中的public字段
        System.out.println(Arrays.toString(clazz.getDeclaredFields()));//获取自身所有字段

        //6.获取方法信息相关
        System.out.println(Arrays.toString(clazz.getMethods()));//获取自身和父类（含接口）的public方法
        System.out.println(Arrays.toString(clazz.getDeclaredMethods()));//获取自身所有方法

        //反射方法的其它使用之---通过反射越过泛型检查
        ArrayList<String> strList = new ArrayList<>();
        strList.add("aaa");
        strList.add("bbb");

        //  strList.add(100);
        //获取ArrayList的Class对象，反向的调用add()方法，添加数据
        Class listClass = strList.getClass(); //得到 strList 对象的字节码 对象
        //获取add()方法
        Method m = listClass.getMethod("add", Object.class);
        //调用add()方法
        m.invoke(strList, 100);

        //遍历集合
        for (Object o : strList) {
            System.out.println(o);
        }

    }

    /**
     * 通过class内省获取bean属性 方法等等
     * <p>
     * 反射可以操作各种类的属性，而内省只是通过反射来操作JavaBean的属性
     * 内省设置属性值肯定会调用setter方法，反射可以不用
     */
    @Test
    public void introspector() throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        //1 获得 java Bean的描述信息
        BeanInfo info = Introspector.getBeanInfo(UserBean.class);
        //2 获得 UserBean中的属性信息
        PropertyDescriptor[] pds = info.getPropertyDescriptors();
        //3 遍历属性信息
        UserBean userBean = new UserBean();
        for (PropertyDescriptor pd : pds) {
            String name = pd.getName();
            if (!"class".equals(name) && "name".equals(name)) {
                Method method = pd.getWriteMethod();
                method.invoke(userBean, "nn");
            }
        }

    }

}
