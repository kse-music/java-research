package cn.hiboot.java.research.design.factory;

public class Circle implements Shape {

    @Override
    public void draw() {
        System.out.println("Shape: Circle::draw()");
    }
}
