package cn.hiboot.java.research.java.socket;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * description about this class
 *
 * @author DingHao
 * @since 2019/8/11 15:10
 */
public class TimeClient {

    @Test
    public void client() {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1", 8080);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            out.println("QUERY TIME ORDER");
            String resp = in.readLine();
            System.out.println("Now is "+resp);
        } catch (IOException e) {

        }finally {
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
            System.out.println("close");
        }
    }
}
