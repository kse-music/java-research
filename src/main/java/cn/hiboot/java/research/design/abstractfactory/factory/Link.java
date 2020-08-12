package cn.hiboot.java.research.design.abstractfactory.factory;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/8 14:12
 */
public abstract class Link extends Item {

    protected String url;

    public Link(String caption,String url) {
        super(caption);
        this.url = url;
    }
}
