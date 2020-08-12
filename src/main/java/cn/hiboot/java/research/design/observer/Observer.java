package cn.hiboot.java.research.design.observer;

public abstract class Observer {
    protected Subject subject;
    public abstract void update();
}
