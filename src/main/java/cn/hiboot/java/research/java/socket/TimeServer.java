package cn.hiboot.java.research.java.socket;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * description about this class
 *
 * @author DingHao
 * @since 2019/8/11 15:04
 */
public class TimeServer {

    @Test
    public void server() throws IOException{
        int port = 8080;
        ServerSocket server = null;
        try  {
            server = new ServerSocket(port);
            System.out.println("start in " + port);
            Socket socket;
            while (true){
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        }catch (Exception e){

        }finally {
            if(server != null){
                System.out.println("close");
                server.close();
                server = null;
            }
        }
    }

}
