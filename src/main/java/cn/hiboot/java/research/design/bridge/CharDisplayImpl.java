package cn.hiboot.java.research.design.bridge;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/8 16:17
 */
public class CharDisplayImpl extends DisplayImpl {

    private char start;
    private char center;
    private char end;

    public CharDisplayImpl(char start, char center, char end) {
        this.start = start;
        this.center = center;
        this.end = end;
    }

    @Override
    public void rawOpen() {
        System.out.print(start);
    }

    @Override
    public void rawPrint() {
        System.out.print(center);

    }

    @Override
    public void rawClose() {
        System.out.println(end);
    }

}
