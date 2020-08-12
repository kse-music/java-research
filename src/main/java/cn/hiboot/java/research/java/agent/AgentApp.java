package cn.hiboot.java.research.java.agent;

import java.lang.instrument.Instrumentation;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/5/3 23:36
 */
public class AgentApp {

    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("enter premain");
        System.out.println(agentOps);
        inst.addTransformer(new Agent());
    }

    public static void agentmain(String agentOps, Instrumentation inst) {
        System.out.println("enter agentmain");
        System.out.println(agentOps);

    }

}
