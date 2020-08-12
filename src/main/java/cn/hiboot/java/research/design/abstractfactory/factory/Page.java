package cn.hiboot.java.research.design.abstractfactory.factory;

import com.google.common.collect.Lists;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/8 14:15
 */
public abstract class Page implements HTMLAble{

    protected String title;
    protected String author;
    protected List<Item> content = Lists.newArrayList();

    public Page(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public void add(Item item){
        content.add(item);
    }

    public void output(){
        String filename = title + ".html";
        try {
            Writer writer = new FileWriter(filename);
            writer.write(makeHtml());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
