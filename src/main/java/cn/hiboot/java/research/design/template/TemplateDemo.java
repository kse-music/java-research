package cn.hiboot.java.research.design.template;

import org.junit.jupiter.api.Test;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/17 23:22
 */
public class TemplateDemo {

    @Test
    public void template() {
        Game game = new Cricket();
        game.play();
        System.out.println();
        game = new Football();
        game.play();

    }
}
