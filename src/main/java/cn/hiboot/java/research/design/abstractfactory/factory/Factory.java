package cn.hiboot.java.research.design.abstractfactory.factory;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/7/8 14:21
 */
public abstract class Factory {

    public static Factory getFactory(String classname){
        Factory factory = null;
        try {
            factory = (Factory) Class.forName(classname).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("没有找到 "+classname+" 类。");
        }
        return factory;
    }

    public abstract Link createLink(String caption,String url);
    public abstract Tray createTray(String caption);
    public abstract Page createPage(String title,String author);
    public abstract Page createYahooPage();

}
