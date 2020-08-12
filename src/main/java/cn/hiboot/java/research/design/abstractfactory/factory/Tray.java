package cn.hiboot.java.research.design.abstractfactory.factory;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 托盘：含有多个Link和Tray的容器
 *
 * @author DingHao
 * @since 2019/7/8 14:12
 */
public abstract class Tray extends Item {

    protected List<Item> tray = Lists.newArrayList();

    public Tray(String caption) {
        super(caption);
    }

    public void add(Item item){
        tray.add(item);
    }

}
