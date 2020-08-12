package cn.hiboot.java.research.design.state;

import org.junit.jupiter.api.Test;

/**
 * 意图：允许对象在内部状态发生改变时改变它的行为，对象看起来好像修改了它的类。
 *
 * @author DingHao
 * @since 2019/7/10 0:26
 */
public class StateDemo {

    @Test
    public void state(){
        Context context = new Context();

        StartState startState = new StartState();
        startState.doAction(context);

        System.out.println(context.getState());

        StopState stopState = new StopState();
        stopState.doAction(context);

        System.out.println(context.getState());
    }

}
