package cn.hiboot.java.research.design.abstractfactory.listfactory;


import cn.hiboot.java.research.design.abstractfactory.factory.Item;
import cn.hiboot.java.research.design.abstractfactory.factory.Tray;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/8 14:34
 */
public class ListTray extends Tray {

    public ListTray(String caption) {
        super(caption);
    }

    @Override
    public String makeHtml() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<li>\n");
        buffer.append(caption + "\n");
        buffer.append("<ul>\n");
        for (Item item : tray) {
            buffer.append(item.makeHtml());
        }
        buffer.append("</ul>\n");
        buffer.append("</li>\n");
        return buffer.toString();
    }
}
