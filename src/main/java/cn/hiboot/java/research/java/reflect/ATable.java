package cn.hiboot.java.research.java.reflect;

import java.lang.annotation.*;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/10/23 10:04
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ATable {
    String name() default "";
}
