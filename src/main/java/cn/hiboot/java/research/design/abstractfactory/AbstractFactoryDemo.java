package cn.hiboot.java.research.design.abstractfactory;

import cn.hiboot.java.research.design.abstractfactory.factory.Factory;
import cn.hiboot.java.research.design.abstractfactory.factory.Link;
import cn.hiboot.java.research.design.abstractfactory.factory.Page;
import cn.hiboot.java.research.design.abstractfactory.factory.Tray;
import org.junit.jupiter.api.Test;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/8 14:06
 */
public class AbstractFactoryDemo {

    @Test
    public void abstractFactory(){

        Factory factory = Factory.getFactory("cn.hiboot.design.abstractfactory.listfactory.ListFactory");

        Link people = factory.createLink("人民日报","");
        Link gmw = factory.createLink("光明日报","");

        Link yahoo = factory.createLink("Yahoo!","");
        Link jp_yahoo = factory.createLink("Yahoo!Japan","");
        Link excite = factory.createLink("Excite","");
        Link google = factory.createLink("Google","");

        Tray tray = factory.createTray("日报");
        tray.add(people);
        tray.add(gmw);

        Tray trayYahoo = factory.createTray("Yahoo!");
        trayYahoo.add(yahoo);
        trayYahoo.add(jp_yahoo);

        Tray traySearch = factory.createTray("检索引擎");
        traySearch.add(trayYahoo);
        traySearch.add(excite);
        traySearch.add(google);

        Page page = factory.createPage("LinkPage","杨文轩");
        page.add(tray);
        page.add(traySearch);
        page.output();

        factory.createYahooPage().output();


    }

}
