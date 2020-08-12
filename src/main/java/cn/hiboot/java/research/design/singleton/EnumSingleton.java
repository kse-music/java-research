package cn.hiboot.java.research.design.singleton;

public enum EnumSingleton {
    //枚举,非懒加载
    INSTANCE;
    private Resource instance;
    EnumSingleton() {
        instance = new Resource();
    }
    public Resource getInstance() {
        return instance;
    }
}

