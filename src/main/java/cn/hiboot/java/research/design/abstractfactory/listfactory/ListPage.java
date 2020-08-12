package cn.hiboot.java.research.design.abstractfactory.listfactory;


import cn.hiboot.java.research.design.abstractfactory.factory.Item;
import cn.hiboot.java.research.design.abstractfactory.factory.Page;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/8 14:34
 */
public class ListPage extends Page {

    public ListPage(String title, String author) {
        super(title, author);
    }

    @Override
    public String makeHtml() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><head><title>" + title + "</title></head>\n");
        buffer.append("<body>\n");
        buffer.append("<h1>" + title + "</h1>\n");
        buffer.append("<ul>\n");
        for (Item item : content) {
            buffer.append(item.makeHtml());
        }
        buffer.append("</ul>\n");
        buffer.append("<hr/><address>" + author + "</address>");
        buffer.append("</body></html>\n");
        return buffer.toString();
    }

}
