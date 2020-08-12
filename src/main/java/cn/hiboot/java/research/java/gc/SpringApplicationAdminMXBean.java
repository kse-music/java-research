package cn.hiboot.java.research.java.gc;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/10/22 11:33
 */
public interface SpringApplicationAdminMXBean {
    boolean isReady();

    boolean isEmbeddedWebApplication();

    String getProperty(String key);

    void shutdown();
}
