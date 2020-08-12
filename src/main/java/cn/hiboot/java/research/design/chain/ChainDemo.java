package cn.hiboot.java.research.design.chain;

import org.junit.jupiter.api.Test;

/**
 * 推卸责任：按照责任链上的顺序执行
 *
 * @author DingHao
 * @since 2019/7/18 13:13
 */
public class ChainDemo {

    @Test
    public void chain(){
        AbstractLogger loggerChain = getChainOfLoggers();

        loggerChain.logMessage(AbstractLogger.INFO,
                "This is an information.");

        loggerChain.logMessage(AbstractLogger.DEBUG,
                "This is an debug level information.");

        loggerChain.logMessage(AbstractLogger.ERROR,
                "This is an error information.");
    }

    private AbstractLogger getChainOfLoggers(){

        AbstractLogger error = new ErrorLogger(AbstractLogger.ERROR);
        AbstractLogger info = new ConsoleLogger(AbstractLogger.INFO);
        AbstractLogger debug = new FileLogger(AbstractLogger.DEBUG);

        debug.setNextLogger(info).setNextLogger(error);

        return debug;
    }
}
