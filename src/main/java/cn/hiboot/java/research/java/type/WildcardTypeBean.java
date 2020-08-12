package cn.hiboot.java.research.java.type;

import java.util.List;

/**
 *  WildcardType represents a wildcard type expression, such as ?, ? extends Number,
 *  * or ? super Integer.
 *  * 通配符表达式，或泛型表达式，它虽然是Type的一个子接口，但并不是Java类型中的一种，表示的仅仅是类似
 *  *  ? extends T、? super K这样的通配符表达式。
 *  * ？---通配符表达式，表示通配符泛型，但是WildcardType并不属于Java-Type中的一钟；
 *  * 例如：List< ? extends Number> 和 List< ? super Integer>；
 *  * 1、Type[] getUpperBounds();  //获得泛型表达式上界（上限） 获取泛型变量的上边界（extends）
 *  * 2、Type[] getLowerBounds(); //获得泛型表达式下界（下限） 获取泛型变量的下边界（super）
 *
 * @author DingHao
 * @since 2020/8/2 23:00
 */
public class WildcardTypeBean {

    /**
     * 1、 a: 获取ParameterizedType:? extends java.lang.Number
     * 2、上界：class java.lang.Number
     */
    private List< ? extends Number> a;

    /**
     * b: 获取ParameterizedType:? super java.lang.String
     *  上届：class java.lang.Object
     *  下届：class java.lang.String
     */
    private List< ? super String> b;

    /**
     * c: 获取ParameterizedType:class java.lang.String
     */
    private List<String> c;

    /**
     * aClass: 获取ParameterizedType:?
     * 上届：class java.lang.Object
     */
    private Class<?> aClass;

    private String str;

}
