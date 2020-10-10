package cn.hiboot.java.research.db.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/11/4 23:34
 */
public class MongoHelper {

    public static MongoClient defaultClient(){
        return mongoClient("192.168.4.30",19130);
    }

    public static MongoClient mongoClient(String ip, int port){
        return mongoClient(ip + ":" + port);
    }

    public static MongoClient mongoClient(String host){
        String[] split = host.split(",");
        return MongoClients.create(
                MongoClientSettings.builder()
//                        .addCommandListener(new CommandListener() {
//                            @Override
//                            public void commandStarted(CommandStartedEvent event) {
//                                System.out.println(event);
//                            }
//
//                            @Override
//                            public void commandSucceeded(CommandSucceededEvent event) {
//                                System.out.println(event);
//                            }
//
//                            @Override
//                            public void commandFailed(CommandFailedEvent event) {
//                                System.out.println(event);
//                            }
//                        })
                        .applyToClusterSettings(builder -> builder.hosts(Arrays.stream(split).map(ServerAddress::new).collect(Collectors.toList())))
                        .build());
    }

    public static MongoClient mongoClient(String host,String username,String password){
        String[] split = host.split(",");
        return MongoClients.create(
                MongoClientSettings.builder()
                        .writeConcern(WriteConcern.UNACKNOWLEDGED)
                        .credential(MongoCredential.createCredential(username,"admin",password.toCharArray()))
                        .applyToClusterSettings(builder -> builder.hosts(Arrays.stream(split).map(ServerAddress::new).collect(Collectors.toList())))
                        .build());
    }

}
