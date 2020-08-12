package cn.hiboot.java.research.design.abstractfactory.listfactory;


import cn.hiboot.java.research.design.abstractfactory.factory.Factory;
import cn.hiboot.java.research.design.abstractfactory.factory.Link;
import cn.hiboot.java.research.design.abstractfactory.factory.Page;
import cn.hiboot.java.research.design.abstractfactory.factory.Tray;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/8 14:33
 */
public class ListFactory extends Factory {

    @Override
    public Link createLink(String caption, String url) {
        return new ListLink(caption, url);
    }

    @Override
    public Tray createTray(String caption) {
        return new ListTray(caption);
    }

    @Override
    public Page createPage(String title, String author) {
        return new ListPage(title, author);
    }

    @Override
    public Page createYahooPage() {
        Page yahoo = createPage("Yahoo!", "Yahoo");
        yahoo.add(createLink("Yahoo",""));
        return yahoo;
    }

}
