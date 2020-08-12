package cn.hiboot.java.research.design.decorator;


import cn.hiboot.java.research.design.factory.Shape;

/**
 * @author DingHao
 * @since 2018/12/9 22:44
 */
public class RedShapeDecorator extends ShapeDecorator {

    public RedShapeDecorator(Shape decoratedShape) {
        super(decoratedShape);
    }

    @Override
    public void draw() {
        decoratedShape.draw();
        setRedBorder(decoratedShape);
    }

    private void setRedBorder(Shape decoratedShape){
        System.out.println("Border Color: Red");
    }
}