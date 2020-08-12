package cn.hiboot.java.research.java.annotation;

/**
 * description about this class
 *
 * @author DingHao
 * @since 2020/5/17 10:56
 */
public class BADLY_NAMED_CODE {
    enum colors{
       red,blue,green;
    }

    static final int _FORTY_TWO = 42;

    public static int NOT_A_CONSTANT = _FORTY_TWO;

    protected void BADLY_NAMED_CODE(){
        return;
    }

    public void NOTcamelCASEmethodNAME(){
        return;
    }
}
