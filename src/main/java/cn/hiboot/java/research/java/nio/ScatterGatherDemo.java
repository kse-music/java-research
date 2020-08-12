package cn.hiboot.java.research.java.nio;


import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;

public class ScatterGatherDemo{

    private String relativelyPath = "H:/sampledata";

    /*
     * gatherBytes() is used for reading the bytes from the buffers and write it
     * to a file channel.
     */
    public void gatherBytes(String data){
        // The First Buffer is used for holding a random number
        ByteBuffer buffer1 = ByteBuffer.allocate(8);
        // The Second Buffer is used for holding a data that we want to write
        ByteBuffer buffer2 = ByteBuffer.allocate(400);
        buffer1.asIntBuffer().put(420);
        buffer2.asCharBuffer().put(data);
        try {
            GatheringByteChannel gatherer = new FileOutputStream(relativelyPath+"/out.txt").getChannel();
            // Write the data into file
            gatherer.write(new ByteBuffer[] { buffer1, buffer2 });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * scatterBytes() is used for reading the bytes from a file channel into a
     * set of buffers.
     */
    public void scatterBytes() {
        ByteBuffer buffer1 = ByteBuffer.allocate(8);
        ByteBuffer buffer2 = ByteBuffer.allocate(400);
        try {
            ScatteringByteChannel scatter = new FileInputStream(relativelyPath+"/out.txt").getChannel();
            // Reading a data from the channel
            scatter.read(new ByteBuffer[] { buffer1, buffer2 });
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Read the two buffers separate
        buffer1.rewind();
        buffer2.rewind();

        int bufferOne = buffer1.asIntBuffer().get();
        String bufferTwo = buffer2.asCharBuffer().toString();
        // Verification of content
        System.out.println(bufferOne);
        System.out.println(bufferTwo);
    }

    @Test
    public void scatterGather() {
        ScatterGatherDemo scatterGatherDemo = new ScatterGatherDemo();
        scatterGatherDemo.gatherBytes("Scattering and Gathering shown in hiboot.cn");
        scatterGatherDemo.scatterBytes();
    }

}
