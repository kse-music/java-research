package cn.hiboot.java.research.design.visitor;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/1/4 10:20
 */
public class Keyboard implements ComputerPart {
    @Override
    public void accept(ComputerPartVisitor computerPartVisitor) {
        computerPartVisitor.visit(this);
    }
}
