package cn.hiboot.java.research.design.state;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/4/9 17:13
 */
public class Context {

    private State state;

    public Context(){
        state = null;
    }

    public void setState(State state){
        this.state = state;
    }

    public State getState(){
        return state;
    }

}
