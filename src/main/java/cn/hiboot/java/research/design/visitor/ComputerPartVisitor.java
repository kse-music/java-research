package cn.hiboot.java.research.design.visitor;

/**
 * 数据结构与处理被分离开来
 *
 * @author DingHao
 * @since 2019/1/4 10:20
 */
public interface ComputerPartVisitor {
    void visit(Computer computer);
    void visit(Mouse mouse);
    void visit(Keyboard keyboard);
    void visit(Monitor monitor);
}
