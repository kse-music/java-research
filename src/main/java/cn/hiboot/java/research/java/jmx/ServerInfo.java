package cn.hiboot.java.research.java.jmx;


public class ServerInfo implements ServerInfoMBean {

    public int getExecutedSqlCmdCount() {

        return 0;
    }

    @Override
    public void printString(String fromJConsole) {
        System.out.println(fromJConsole);
    }

}
