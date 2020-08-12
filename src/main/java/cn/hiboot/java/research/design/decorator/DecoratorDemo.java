package cn.hiboot.java.research.design.decorator;

import cn.hiboot.java.research.design.factory.Circle;
import cn.hiboot.java.research.design.factory.Rectangle;
import cn.hiboot.java.research.design.factory.Shape;
import org.junit.jupiter.api.Test;

/**
 * 装饰器模式（Decorator Pattern）允许向一个现有的对象添加新的功能，同时又不改变其结构。
 * 这种类型的设计模式属于结构型模式，它是作为现有的类的一个包装。
 *
 * 这种模式创建了一个装饰类，用来包装原有的类，并在保持类方法签名完整性的前提下，提供了额外的功能。
 *
 * @author DingHao
 * @since 2019/7/17 23:20
 */
public class DecoratorDemo {

    @Test
    public void decorator(){
        Shape circle = new Circle();

        Shape redCircle = new RedShapeDecorator(new Circle());

        Shape redRectangle = new RedShapeDecorator(new Rectangle());
        System.out.println("Circle with normal border");
        circle.draw();

        System.out.println("\nCircle of red border");
        redCircle.draw();

        System.out.println("\nRectangle of red border");
        redRectangle.draw();
    }
}
