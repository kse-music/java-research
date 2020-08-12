package cn.hiboot.java.research.java.simple;


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * simple demo
 *
 * @author DingHao
 * @since 2018/12/11 15:41
 */
@Slf4j
public class SimpleDemo {

    @Test
    public void simple() {
        TestEnum[] values = TestEnum.values();
        TestEnum in = TestEnum.valueOf(TestEnum.INTEGER.toString());

        for (TestEnum value : values) {
            System.out.println(value);
        }
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i < 10000000; i++) {
            list.add(i);
        }
        long s = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            operation(list, d -> {
//            System.out.println(d);
            });
            System.out.println(System.currentTimeMillis() - s);
        }
    }

    <S> void operation(List<S> all, Consumer<List<S>> consumer) {
        int pageSize = 100;
//        int num = 0;
//        List<S> data = Lists.newArrayListWithCapacity(pageSize);
//        for (S o : all) {
//            num++;
//            data.add(o);
//            if (num % pageSize == 0) {
//                consumer.accept(data);
//                data.clear();
//            }
//        }
//        if (!data.isEmpty()) {
//            consumer.accept(data);
//            data.clear();
//        }
        int count = (all.size() - 1) / pageSize + 1;
        if (count == 1) {
            consumer.accept(all);
        }
        int toIndex;
        for (int i = 0; i < count; i++) {
            if (i == count - 1) {
                toIndex = all.size();
            } else {
                toIndex = (i + 1) * pageSize;
            }
            consumer.accept(all.subList(i * pageSize, toIndex));
        }
    }

    /**
     * 一、使用clazz.getClassLoader().getResourceAsStream(fileName)方式
     * 1.它只会解析一个配置文件
     * 2.顺序是：本体项目 -> maven引入jar中的第一个
     * <p>
     * 二、使用spring工具类，Properties globalProperties = PropertiesLoaderUtils.loadAllProperties("console.properties");
     * 1.它会解析classpath*下所有的文件
     * 2.顺序是：本体项目 -> maven中引入顺序（从上到下）
     * 3. 相同key后面的覆盖前面的
     */
    @Test
    public void loadResource() {
        Properties prop = new Properties();
        try {
            //下面这两方式,如果不是 / 开头会拼接当前class的路径查找,是则再到class路径根目录查找
//            InputStream in = getClass().getResource("demo.properties").openStream();
//            InputStream in = getClass().getResourceAsStream("demo.properties");

            //下面这两方式不管有没有 / 都是从class的根目录查找
            InputStream in = getClass().getClassLoader().getResource("demo.properties").openStream();
//            InputStream in = getClass().getClassLoader().getResourceAsStream("demo.properties");
            prop.load(in);
        } catch (Exception e) {
            log.error("{}", e);
        }
        log.info("{}", prop);

        log.info("{}", getClass().getResource("").getPath());
        log.info("{}", getClass().getClassLoader().getResource("").getPath());

    }

    @Test
    public void mac() throws SocketException {

        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        while (nets.hasMoreElements()) {
            byte[] mac = nets.nextElement().getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            if (mac != null) {
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                log.info(sb.toString().toUpperCase());
            }
        }

    }

    /**
     * java执行js
     */
    @Test
    public void js() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        try {
            log.info("Result:" + engine.eval("function f() { return 1; }; f() + 1;"));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bm() {
        int[] num = {1, 5, 30, 32, 64, 56, 159, 120, 21, 17, 35, 45};
        BitMap map = new BitMap(10000000);
        for (int value : num) {
            map.add(value);
        }
        int temp = 120;
        if (map.exist(temp)) {
            System.out.println("temp:" + temp + " has already exists");
        }
    }

}
