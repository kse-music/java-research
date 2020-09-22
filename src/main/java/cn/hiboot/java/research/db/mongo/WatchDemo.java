package cn.hiboot.java.research.db.mongo;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.Document;
import org.junit.jupiter.api.Test;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/11/4 23:32
 */
public class WatchDemo extends MongoTest{

    @Test
    public void watchMongoClient(){
        for (ChangeStreamDocument<Document> watch : mongoClient.watch().fullDocument(FullDocument.UPDATE_LOOKUP)) {
            System.out.println(watch);
        }
    }

    @Test
    public void watchDb(){
        for (ChangeStreamDocument<Document> watch : mongoDatabase.watch()) {
            System.out.println(watch);
        }
    }

    @Test
    public void watchCollection(){
        for (ChangeStreamDocument<Document> watch : mongoDatabase.getCollection("test").watch()) {
            System.out.println(watch);
        }
    }
}
