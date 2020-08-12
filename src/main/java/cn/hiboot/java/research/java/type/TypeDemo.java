package cn.hiboot.java.research.java.type;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;

/**
 * Type体系中类型的包括：原始类型(Class)、参数化类型(ParameterizedType)、数组类型(GenericArrayType)、类型变量(TypeVariable)、基本类型(Class);
 *
 * 原始类型，不仅仅包含我们平常所指的类，还包括枚举、数组、注解等；
 * 参数化类型，就是我们平常所用到的泛型List、Map；
 * 数组类型，并不是我们工作中所使用的数组String[] 、byte[]，而是带有泛型的数组，即T[] ；
 * 基本类型，也就是我们所说的java的基本类型，即int,float,double等
 *
 * @author DingHao
 * @since 2020/8/2 18:22
 */
@Slf4j
public class TypeDemo {


    @Test
    public void parameterizedTypeTest(){
        try {
            Field[] fields = ParameterizedTypeBean.class.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                Type genericType = field.getGenericType();
//                if (genericType instanceof ParameterizedType) {
//                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
//                    for (Type type : parameterizedType.getActualTypeArguments()) {
//                        log.info(name + ": 获取ParameterizedType:" + type);
//                    }
//                    Type ownerType = parameterizedType.getOwnerType();
//                    if (ownerType != null) {
//                        log.info(name + ":getOwnerType:" + ownerType);
//                    } else {
//                        log.info(name + ":getOwnerType is null");
//                    }
//                    Type rawType = parameterizedType.getRawType();
//                    if (rawType != null) {
//                        log.info(name + ":getRawType:" + rawType);
//                    }
//                } else {
//                    log.info(name + ":is not ParameterizedType ");
//                }
                parse(genericType);
            }
        }catch (Exception e){
            log.error("error",e);
        }
    }


    @Test
    public void genericArrayTypeTest(){
        /*
         * 1、getGenericComponentType
         * 返回泛型数组中元素的Type类型，即List<String>[] 中的 List<String>（ParameterizedTypeImpl）
         * 、T[] 中的T（TypeVariableImpl）；
         * 值得注意的是，无论是几维数组，getGenericComponentType()方法都只会脱去最右边的[]，返回剩下的值；
         */
        Method[] declaredMethods = GenericArrayTypeBean.class.getDeclaredMethods();
        for(Method method :declaredMethods){
            /*
             * 获取当前参数所有的类型信息
             */
            Type[] types = method.getGenericParameterTypes();
            for(Type type: types){
//                if(type instanceof ParameterizedType){
//                    log.info("ParameterizedType type :"+type);
//                }else if(type instanceof GenericArrayType){
//                    log.info("GenericArrayType type :"+type);
//                    Type genericComponentType = ((GenericArrayType) type).getGenericComponentType();
//                    /*
//                     * 获取泛型数组中元素的类型，要注意的是：无论从左向右有几个[]并列，
//                     * 这个方法仅仅脱去最右边的[]之后剩下的内容就作为这个方法的返回值。
//                     */
//                    log.info("genericComponentType:"+genericComponentType);
//                }else if(type instanceof WildcardType){
//                    log.info("WildcardType type :"+type);
//                }else if(type instanceof  TypeVariable){
//                    log.info("TypeVariable type :"+type);
//                }else {
//                    log.info("type :"+type);
//                }
                parse(type);
            }
        }
    }

    /**
     * 从这个例子中可以看出来各种类型之间是相互在使用的
     * TypeVariable<D extends GenericDeclaration>
     * GenericDeclaration  All Known Implementing Classes: Class, Constructor, Method
     */
    @Test
    public void typeVariableTest(){
        try {
            Field[] fields = TypeVariableBean.class.getDeclaredFields();
            for (Field f : fields) {
                String name = f.getName();
                Type genericType = f.getGenericType();
//                if (genericType instanceof ParameterizedType) {
//                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
//                    for (Type type : parameterizedType.getActualTypeArguments()) {
//                        log.info(f.getName() + ": 获取ParameterizedType:" + type);
//                        if (type instanceof TypeVariable) {
//                            printTypeVariable(name, (TypeVariable) type);
//                        }
//                    }
//                    if (parameterizedType.getOwnerType() != null) {
//                        log.info(name + ":getOwnerType:" + parameterizedType.getOwnerType());
//                    } else {
//                        log.info(name + ":getOwnerType is null");
//                    }
//                    if (parameterizedType.getRawType() != null) {
//                        log.info(name + ":getRawType:" + parameterizedType.getRawType());
//                    }
//                } else if (genericType instanceof GenericArrayType) {
//                    GenericArrayType genericArrayType = (GenericArrayType) genericType;
//                    log.info("GenericArrayType type :" + genericArrayType);
//                    Type genericComponentType = genericArrayType.getGenericComponentType();
//                    if (genericComponentType instanceof TypeVariable) {
//                        TypeVariable typeVariable = (TypeVariable) genericComponentType;
//                        printTypeVariable(name, typeVariable);
//                    }
//                } else if (genericType instanceof TypeVariable) {
//                    TypeVariable typeVariable = (TypeVariable) genericType;
//                    printTypeVariable(name, typeVariable);
//                } else {
//                    log.info("type :" + f.getGenericType());
//                }
                parse(genericType);
            }
        } catch (Exception e) {
            log.error("error", e);
        }
    }

    @Test
    public void wildcardTypeTest(){
        try {
            Field[] fields = WildcardTypeBean.class.getDeclaredFields();
            for (Field f : fields) {
                Type genericType = f.getGenericType();
//                if (genericType instanceof ParameterizedType) {
//                    ParameterizedType parameterizedType = (ParameterizedType) f.getGenericType();
//                    for (Type type : parameterizedType.getActualTypeArguments()) {
//                        log.info(f.getName() + ": 获取ParameterizedType:" + type);
//                        if (type instanceof WildcardType) {
//                            printWildcardType(f.getName,(WildcardType) type);
//                        }
//                    }
//                } else if (genericType instanceof GenericArrayType) {
//                    GenericArrayType genericArrayType = (GenericArrayType) f.getGenericType();
//                    log.info("GenericArrayType type :" + genericArrayType);
//                    Type genericComponentType = genericArrayType.getGenericComponentType();
//                    if (genericComponentType instanceof WildcardType) {
//                        printWildcardType((WildcardType) genericComponentType);
//                    }
//                } else if (genericType instanceof TypeVariable) {
//                    TypeVariable typeVariable = (TypeVariable) f.getGenericType();
//                    log.info("typeVariable:" + typeVariable);
//                } else {
//                    log.info("type :" + f.getGenericType());
//                    if (genericType instanceof WildcardType) {
//                        printWildcardType((WildcardType) f.getGenericType());
//                    }
//                }
                parse(genericType);
            }
        } catch (Exception e) {
            log.error("error", e);
        }

    }

    @Test
    public void parentType() {
        /*
         * 这里是获取父类中泛型，如果有多个也是一样的方式哈哈！获取到的泛型参数还可能是 通配符表达式，
         * 这里也是可以处理的，多个判断而已
         */
        Type genericSuperclassType = Children.class.getGenericSuperclass();
        if (genericSuperclassType instanceof ParameterizedType) {
//            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclassType).getActualTypeArguments();
//            for (Type argumentType : actualTypeArguments) {
//                log.info("父类ParameterizedType.getActualTypeArguments:" + argumentType);
//            }
            parse(genericSuperclassType);

        }
        /*
         * 这里获取父接口中的泛型参数
         */
        Type[] genericInterfacesTypes = Children.class.getGenericInterfaces();
        for (Type interfaceType : genericInterfacesTypes) {
            if (interfaceType instanceof ParameterizedType) {
//                Type[] actualTypeArguments = ((ParameterizedType) interfaceType).getActualTypeArguments();
//                for (Type argumentType : actualTypeArguments) {
//                    log.info("父接口ParameterizedType.getActualTypeArguments:" + argumentType);
//                }
                parse(interfaceType);
            }
        }
        /*
         言归正传，下面讲解ResolvableType。ResolvableType为所有的java类型提供了统一的数据结构以及API
         ，换句话说，一个ResolvableType对象就对应着一种java类型。
         我们可以通过ResolvableType对象获取类型携带的信息
         （举例如下）：
         1.getSuperType()：获取直接父类型
         2.getInterfaces()：获取接口类型
         3.getGeneric(int...)：获取类型携带的泛型类型
         4.resolve()：Type对象到Class对象的转换

         另外，ResolvableType的构造方法全部为私有的，我们不能直接new，只能使用其提供的静态方法进行类型获取：
         1.forField(Field)：获取指定字段的类型
         2.forMethodParameter(Method, int)：获取指定方法的指定形参的类型
         3.forMethodReturnType(Method)：获取指定方法的返回值的类型
         4.forClass(Class)：直接封装指定的类型
         */
//        ResolvableType superResolvableType = ResolvableType.forClass(Children.class).getSuperType();
//        log.info("supper:"+superResolvableType.resolveGenerics()[0]);
//
//        ResolvableType superInterfaceResolvableType = ResolvableType.forClass(Children.class).getInterfaces()[0];
//        log.info("supper:"+superInterfaceResolvableType.resolveGenerics()[0]);

    }


    private void parse(Type type){
        if(type instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType)type;
            log.info("ParameterizedType type :"+type);
            for (Type t : parameterizedType.getActualTypeArguments()) {
                print(t);
            }
            Type ownerType = parameterizedType.getOwnerType();
            if (ownerType != null) {
                log.info(":getOwnerType:" + ownerType);
            } else {
                log.info(":getOwnerType is null");
            }
            Type rawType = parameterizedType.getRawType();
            if (rawType != null) {
                log.info(":getRawType:" + rawType);
            }
        }else if(type instanceof GenericArrayType){
            log.info("GenericArrayType type :"+type);
            Type genericComponentType = ((GenericArrayType) type).getGenericComponentType();
            /*
             * 获取泛型数组中元素的类型，要注意的是：无论从左向右有几个[]并列，
             * 这个方法仅仅脱去最右边的[]之后剩下的内容就作为这个方法的返回值。
             */
            log.info("genericComponentType:"+genericComponentType);
            print(genericComponentType);
        }else {
            print(type);
        }
    }

    /**
     * 1、Type[] getBounds() 类型对应的上限，默认为Object
     * 2、D getGenericDeclaration()  获取声明该类型变量实体，也就是TypeVariableBean<T>中的TypeVariableBean
     * 3、String getName() 获取类型变量在源码中定义的名称；
     */
    private void print(Type t){
        if (t instanceof WildcardType) {
            log.info("WildcardType type :"+t);
            WildcardType wildcardType = (WildcardType)t;
            for (Type type : wildcardType.getUpperBounds()) {
                log.info("上界：" + type);
            }
            for (Type type : wildcardType.getLowerBounds()) {
                log.info("下界：" + type);
            }
        }else if (t instanceof TypeVariable) {
            log.info("TypeVariable type :"+t);
            TypeVariable typeVariable = (TypeVariable)t;
            for (Type type : typeVariable.getBounds()) {
                log.info(": TypeVariable getBounds " + type);
            }
            log.info("定义Class getGenericDeclaration: " + typeVariable.getGenericDeclaration());
            log.info("getName: " + typeVariable.getName());
        }else {
            log.info("type :"+t);
        }
    }

}
