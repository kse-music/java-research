package cn.hiboot.java.research.db.mongo;

import cn.hiboot.java.research.db.mongo.MongoHelper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2020/9/21 22:26
 */
public abstract class MongoTest {
    protected MongoClient mongoClient;
    protected MongoDatabase mongoDatabase;

    @BeforeEach
    public void init(){
        mongoClient = MongoHelper.defaultClient();
        mongoDatabase = mongoClient.getDatabase("test");
    }

    @AfterEach
    public void end(){
        if(mongoClient != null){
            mongoClient.close();
        }
    }

}
