package cn.hiboot.java.research.java.concurrent;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * description about this class
 *
 * @author DingHao
 * @since 2020/11/14 16:32
 */
public class MyThreadPoolExecutor extends ThreadPoolExecutor {

    private final int workQueueSize;

    public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, int workQueueSize){
        super(corePoolSize,maximumPoolSize,0,TimeUnit.MICROSECONDS,new LinkedBlockingDeque<>(workQueueSize));
        this.workQueueSize = workQueueSize;
    }

    @Override
    public void execute(Runnable runnable){
        while (blocking()) {
        }
        super.execute(runnable);
    }
    public boolean blocking() {
        return getPoolSize() == getMaximumPoolSize() && getQueue().size() == workQueueSize;
    }

    public void closeUntilAllTaskFinish(){
        shutdown();
        while (!isTerminated()){

        }
    }

}
