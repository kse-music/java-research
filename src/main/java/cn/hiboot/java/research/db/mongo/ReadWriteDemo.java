package cn.hiboot.java.research.db.mongo;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mongodb.MongoClient;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/11/4 23:32
 */
public class ReadWriteDemo {

    @Test
    public void write2Mongo() throws IOException {
        MongoClient mongoClient = MongoHelper.mongoClient("192.168.1.64",19130);

        BufferedReader br = Files.newBufferedReader(Paths.get("G:\\data\\tt.txt"));
        String line = null;
        long s = System.currentTimeMillis();
        int maxSize = 300000;
        List<Document> list = Lists.newArrayListWithCapacity(maxSize);
        while ((line = br.readLine()) != null) {
            list.add(Document.parse(line));
            if(list.size() == maxSize){
                mongoClient.getDatabase("test").getCollection("attribute_object").insertMany(list,new InsertManyOptions().ordered(false).bypassDocumentValidation(false));
                list.clear();
                System.out.println(maxSize + " cost " + (System.currentTimeMillis() - s)/1000 + "s");
            }
        }
        if(list.size() > 0){
            mongoClient.getDatabase("test").getCollection("attribute_object").insertMany(list,new InsertManyOptions().ordered(false).bypassDocumentValidation(false));
        }
        System.out.println(System.currentTimeMillis() - s + "ms");
        br.close();
    }

    @Test
    public void readWrite() throws IOException {
        BufferedReader br = Files.newBufferedReader(Paths.get("G:\\data\\twitter_rv2.net"));
        BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get("G:\\data\\ttt.txt"));
        String line;
        StringBuilder sb = new StringBuilder();
        long s = System.currentTimeMillis();
        //地址取一个，推广人去重
        Set<String> tgr = Sets.newHashSet();
        String last = "";
        while ((line = br.readLine()) != null) {
            String[] rs = line.split(",");
            sb.setLength(0);
            sb.append("{");
            sb.append("\"entity_id\"").append(":").append(rs[1]).append(",");
            sb.append("\"entity_type\"").append(":").append(1).append(",");
            sb.append("\"attr_id\"").append(":").append(1).append(",");//关注的人
            sb.append("\"attr_value\"").append(":").append(rs[0]).append(",");
            sb.append("\"attr_value_type\"").append(":").append(1);
            sb.append("}");
            bufferedWriter.write(sb.toString());
            bufferedWriter.newLine();

            if(!rs[0].equals(last)){
                last = rs[0];
                tgr.clear();
                sb.setLength(0);
                sb.append("{");
                sb.append("\"entity_id\"").append(":").append(rs[0]).append(",");
                sb.append("\"entity_type\"").append(":").append(1).append(",");
                sb.append("\"attr_id\"").append(":").append(2).append(",");//地址
                sb.append("\"attr_value\"").append(":").append(Integer.valueOf(rs[2]) + 500000000).append(",");
                sb.append("\"attr_value_type\"").append(":").append(2);
                sb.append("}");
                bufferedWriter.write(sb.toString());
                bufferedWriter.newLine();
            }
            if (!rs[0].equals(rs[3]) && !tgr.contains(rs[3])) {
                tgr.add(rs[3]);
                sb.setLength(0);
                sb.append("{");
                sb.append("\"entity_id\"").append(":").append(rs[0]).append(",");
                sb.append("\"entity_type\"").append(":").append(1).append(",");
                sb.append("\"attr_id\"").append(":").append(3).append(",");//推荐人
                sb.append("\"attr_value\"").append(":").append(rs[3]).append(",");
                sb.append("\"attr_value_type\"").append(":").append(1);
                sb.append("}");
                bufferedWriter.write(sb.toString());
                bufferedWriter.newLine();
            }
        }
        System.out.println(System.currentTimeMillis() - s + "ms");
        br.close();
        bufferedWriter.close();
    }
}
