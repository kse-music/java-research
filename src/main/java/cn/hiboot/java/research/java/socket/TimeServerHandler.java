package cn.hiboot.java.research.java.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * description about this class
 *
 * @author DingHao
 * @since 2019/8/11 15:08
 */
public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(), true);
            String body = null;
            while (true) {
                body = in.readLine();
                if (body == null) {
                    break;
                }
                System.out.println(body);
                out.println("QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER");
            }

        } catch (IOException e) {
            System.out.println("Error." + e);
            try {
                in.close();
            } catch (IOException e1) {

            }
            try {
                this.socket.close();
            } catch (IOException e1) {

            }
            out.close();
            this.socket = null;
        }
    }
}
