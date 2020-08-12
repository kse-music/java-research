package cn.hiboot.java.research.java.gc;


import cn.hiboot.java.research.java.jmx.ServerInfo;

import javax.management.*;
import javax.management.remote.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/10/12 15:26
 */
public class JConsole {
    static class OOMObject {
        public byte[] placeHolder = new byte[64 * 1024];
    }

    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Thread.sleep(50);
            list.add(new OOMObject());
        }
        System.gc();
    }

    public static void main(String[] args) throws Exception {
//        fillHeap(1000);
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("serverInfoMBean:name=serverInfo");
        server.registerMBean(new ServerInfo(), name);

        //连接远程java程序
        LocateRegistry.createRegistry(8081);
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://127.0.0.1:8081/jmxrmi");
        JMXConnectorServer jcs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
        jcs.start();

        useSpringMBean();

    }

    public static void useSpringMBean() throws Exception {

        JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://192.168.1.119:12345/jmxrmi");

        JMXConnector connect = JMXConnectorFactory.connect(jmxServiceURL, null);

        MBeanServerConnection mBeanServerConnection = connect.getMBeanServerConnection();

        String[] domains = mBeanServerConnection.getDomains();

        for (int i = 0; i < domains.length; i++) {
            System.out.printf("domian[%d] = %s \n", i, domains[i]);
        }
        // 注册名和之前server的一致
        ObjectName objectName = new ObjectName("org.springframework.boot:type=Admin,name=SpringApplication");

//        changeParams(mBeanServerConnection, objectName);

        useMethod(mBeanServerConnection, objectName);

        getParam(mBeanServerConnection, objectName);
    }

    /**
     * 对method的调用, 采用反射的方式进行
     */
    public static void useMethod(MBeanServerConnection connection, ObjectName objectName) {
        SpringApplicationAdminMXBean mxBean = MBeanServerInvocationHandler.newProxyInstance(connection, objectName, SpringApplicationAdminMXBean.class, true);
        if(mxBean.isReady()){
            mxBean.shutdown();
        }
        System.out.println(mxBean.isReady());

    }

    /**
     * 可进行相关参数修改
     * 通过setAttribute、getAttrubute方法来进行操作，则属性的首字母要大写
     */
    public static void changeParams(MBeanServerConnection mBeanServerConnection, ObjectName objectName) throws AttributeNotFoundException, InvalidAttributeValueException, ReflectionException, IOException, InstanceNotFoundException, MBeanException {
        mBeanServerConnection.setAttribute(objectName, new Attribute("Ready", false));
        mBeanServerConnection.setAttribute(objectName, new Attribute("EmbeddedWebApplication", false));
    }

    /**
     * 获取参数
     */
    public static void getParam(MBeanServerConnection connection, ObjectName objectName) throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, IOException {
        boolean ready = (boolean) connection.getAttribute(objectName, "Ready");
        boolean embeddedWebApplication = (boolean) connection.getAttribute(objectName, "EmbeddedWebApplication");
        System.out.println(ready + " " + embeddedWebApplication);
    }


}
