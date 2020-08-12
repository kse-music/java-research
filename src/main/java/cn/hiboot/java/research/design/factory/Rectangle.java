package cn.hiboot.java.research.design.factory;

public class Rectangle implements Shape {

    @Override
    public void draw() {
        System.out.println("Shape: Rectangle::draw()");
    }
}
