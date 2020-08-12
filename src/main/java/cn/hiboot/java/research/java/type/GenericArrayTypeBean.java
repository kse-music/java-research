package cn.hiboot.java.research.java.type;

import java.util.List;

/**
 *  GenericArrayType—— 泛型数组
 *  泛型数组，描述的是形如：A<T>[]或T[]类型
 *
 * @author DingHao
 * @since 2020/8/2 22:32
 */
public class GenericArrayTypeBean<T> {
    /**
     * 含有泛型数组的才是GenericArrayType
     * @param pTypeArray GenericArrayType type :java.util.List<java.lang.String>[];
     * genericComponentType:java.util.List<java.lang.String>
     * @param vTypeArray  GenericArrayType type :T[];genericComponentType:T
     * @param list ParameterizedType type :java.util.List<java.lang.String>;
     * @param strings type :class [Ljava.lang.String;
     * @param test type :class [Lcn.hiboot.java.type.GenericArrayTypeBean;
     */
    public void testGenericArrayType(List<String>[] pTypeArray, T[] vTypeArray, List<String> list, String[] strings, GenericArrayTypeBean[] test) {

    }
}
