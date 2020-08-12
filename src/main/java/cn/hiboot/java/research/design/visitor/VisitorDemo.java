package cn.hiboot.java.research.design.visitor;

import org.junit.jupiter.api.Test;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/17 23:21
 */
public class VisitorDemo {

    @Test
    public void visitor() {

        ComputerPart computer = new Computer();

        computer.accept(new ComputerPartDisplayVisitor());

    }
}
