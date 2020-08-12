package cn.hiboot.java.research.design.observer;


import org.junit.jupiter.api.Test;

/**
 * 设计模式（Design pattern）代表了最佳的实践，通常被有经验的面向对象的软件开发人员所采用。
 * 设计模式是软件开发人员在软件开发过程中面临的一般问题的解决方案。
 * 这些解决方案是众多软件开发人员经过相当长的一段时间的试验和错误总结出来的。
 *
 * @author DingHao
 * @since 2019/1/4 10:23
 */
public class ObserverDemo {

    @Test
    public void observer() {
        Subject subject = new Subject();

        new HexObserver(subject);
        new OctalObserver(subject);
        new BinaryObserver(subject);

        System.out.println("First state change: 15");
        subject.setState(15);

        System.out.println("Second state change: 10");
        subject.setState(10);
    }

}
