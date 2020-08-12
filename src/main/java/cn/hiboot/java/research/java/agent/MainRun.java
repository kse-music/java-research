package cn.hiboot.java.research.java.agent;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.List;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/5/3 23:42
 */
public class MainRun {

    public static void main(String[] args) throws Exception {
        //java -javaagent:agent.jar MainRun
        hello("world");

        /**
         * 获取机器上运行的所有jvm的进程id
         *
         * 选择要诊断的jvm
         *
         * 将jvm使用attach函数链接上
         *
         * 使用loadAgent函数加载agent，动态修改字节码
         *
         * 卸载jvm
         */
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : list) {
            if(vmd.displayName().endsWith("MainRun")){
                VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                virtualMachine.loadAgent("agent.jar","...");

                virtualMachine.detach();
            }
        }
    }

    private static void hello(String name) {
        System.out.println("hello " + name);
    }


}
