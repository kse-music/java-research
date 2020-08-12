package cn.hiboot.java.research.design.decorator;


import cn.hiboot.java.research.design.factory.Shape;

/**
 * @author DingHao
 * @since 2018/12/9 22:43
 */
public abstract class ShapeDecorator implements Shape {
    protected Shape decoratedShape;

    public ShapeDecorator(Shape decoratedShape){
        this.decoratedShape = decoratedShape;
    }

    public void draw(){
        decoratedShape.draw();
    }
}