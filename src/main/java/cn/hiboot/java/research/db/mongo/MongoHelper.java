package cn.hiboot.java.research.db.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/11/4 23:34
 */
public class MongoHelper {

    public static MongoClient mongoClient(String ip,int port){
        MongoClientOptions options = MongoClientOptions.builder().build();
        return new MongoClient(new ServerAddress(ip, port), options);
    }

    public static MongoClient mongoClient(String host){
        String[] split = host.split(",");
        MongoClientOptions options = MongoClientOptions.builder().build();
        return new MongoClient(Arrays.stream(split).map(ServerAddress::new).collect(Collectors.toList()), options);
    }

}
