package cn.hiboot.java.research.java.simple;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/8/12 10:15
 */
public enum TestEnum {

    INTEGER(1,"整数值"),
    FLOAT(1,"浮点值");

    Integer type;
    String displayName;

    TestEnum(Integer type, String displayName) {
        this.type = type;
        this.displayName = displayName;
    }
}
