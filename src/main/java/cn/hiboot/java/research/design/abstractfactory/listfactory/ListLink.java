package cn.hiboot.java.research.design.abstractfactory.listfactory;


import cn.hiboot.java.research.design.abstractfactory.factory.Link;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/8 14:34
 */
public class ListLink extends Link {

    public ListLink(String caption, String url) {
        super(caption, url);
    }

    @Override
    public String makeHtml() {
        return " <li><a href = ''>"+caption+"</a></li>";
    }

}
