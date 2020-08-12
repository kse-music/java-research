package cn.hiboot.java.research.design.bridge;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/9 10:31
 */
public class IncreaseDisplay extends CountDisplay {

    private int step;

    public IncreaseDisplay(DisplayImpl display, int step) {
        super(display);
        this.step = step;
    }

    public void show(int times) {
        int count = 0;
        for (int i = 0; i < times; i++) {
            multiDisplay(count);
            count += step;
        }
    }
}
