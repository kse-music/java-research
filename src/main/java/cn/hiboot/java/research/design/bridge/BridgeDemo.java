package cn.hiboot.java.research.design.bridge;

import org.junit.jupiter.api.Test;

/**
 * 桥接模式,使得具体类与接口实现者类无关。
 *
 *
 * 类的功能层次结构
 * 类的实现层次结构
 *
 * 类的层次关系不应过深
 * @author DingHao
 * @since 2019/7/8 15:10
 */
public class BridgeDemo {

    @Test
    public void bridge(){
        Display display1 = new Display(new StringDisplayImpl("Hello, China."));
        Display display2 = new CountDisplay(new StringDisplayImpl("Hello, World."));
        CountDisplay display3 = new CountDisplay(new StringDisplayImpl("Hello, Universe."));
        RandomDisplay display4 = new RandomDisplay(new StringDisplayImpl("Hello, Random."));
        IncreaseDisplay display5 = new IncreaseDisplay(new CharDisplayImpl('<','*','>'),1);
        display1.display();
        display2.display();
        display3.display();
        display3.multiDisplay(5);
        display4.randomDisplay(5);
        display5.show(4);
        new IncreaseDisplay(new CharDisplayImpl('|','#','-'),2).show(6);
    }

}
