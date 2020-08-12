package cn.hiboot.java.research.design.proxy;

import org.junit.jupiter.api.Test;

/**
 * 意图：为其他对象提供一种代理以控制对这个对象的访问。
 *
 * @author DingHao
 * @since 2019/7/21 17:03
 */
public class ProxyDemo {
    @Test
    public void proxy() {
        Image image = new ProxyImage("test_10mb.jpg");

        //图像将从磁盘加载
        image.display();
        System.out.println("");
        // 图像不需要从磁盘加载
        image.display();
    }
}
