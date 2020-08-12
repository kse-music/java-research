package cn.hiboot.java.research.design.factory;

public class Square implements Shape {

    @Override
    public void draw() {
        System.out.println("Shape: Square::draw()");
    }
}
