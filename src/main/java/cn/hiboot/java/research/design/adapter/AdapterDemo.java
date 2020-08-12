package cn.hiboot.java.research.design.adapter;

import org.junit.jupiter.api.Test;

/**
 * 适配器模式,可以统一两个不兼容的接口
 *
 * @author DingHao
 * @since 2019/7/17 23:22
 */
public class AdapterDemo {

    @Test
    public void adapter(){
        Player myPlayer = new MyPlayer();

        myPlayer.play("h.mp3");
        myPlayer.play("me.avi");
    }

}
