package cn.hiboot.java.research.design.chain;

public abstract class AbstractLogger {
    public static int INFO = 1;
    public static int DEBUG = 2;
    public static int ERROR = 3;

    protected int level;

    //责任链中的下一个元素
    protected AbstractLogger nextLogger;

    public AbstractLogger setNextLogger(AbstractLogger nextLogger){
        this.nextLogger = nextLogger;
        return nextLogger;
    }

    public void logMessage(int level, String message){
        if(this.level <= level){
            write(message);
        }else if(nextLogger !=null){
            nextLogger.logMessage(level, message);
        }else {

        }
    }

    abstract protected void write(String message);

}
