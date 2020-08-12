package cn.hiboot.java.research.design.visitor;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/1/4 10:19
 */
public interface ComputerPart {
    void accept(ComputerPartVisitor computerPartVisitor);
}
