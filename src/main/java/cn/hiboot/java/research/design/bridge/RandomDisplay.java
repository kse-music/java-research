package cn.hiboot.java.research.design.bridge;

import java.util.Random;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/8 16:10
 */
public class RandomDisplay extends CountDisplay {

    private Random random = new Random();

    public RandomDisplay(DisplayImpl display) {
        super(display);
    }

    public void randomDisplay(int times){
        multiDisplay(random.nextInt(times));
    }
}
