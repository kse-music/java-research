package cn.hiboot.java.research.design.abstractfactory.factory;

/**
 * 零件
 *
 * @author DingHao
 * @since 2019/7/8 14:11
 */
public abstract class Item implements HTMLAble{

    protected String caption;

    public Item(String caption) {
        this.caption = caption;
    }

}
