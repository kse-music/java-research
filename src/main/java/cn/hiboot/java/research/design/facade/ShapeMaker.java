package cn.hiboot.java.research.design.facade;


import cn.hiboot.java.research.design.factory.Circle;
import cn.hiboot.java.research.design.factory.Rectangle;
import cn.hiboot.java.research.design.factory.Shape;
import cn.hiboot.java.research.design.factory.Square;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/4/11 12:27
 */
public class ShapeMaker {
    private Shape circle;
    private Shape rectangle;
    private Shape square;

    public ShapeMaker() {
        circle = new Circle();
        rectangle = new Rectangle();
        square = new Square();
    }

    public void drawCircle(){
        circle.draw();
    }
    public void drawRectangle(){
        rectangle.draw();
    }
    public void drawSquare(){
        square.draw();
    }
}
