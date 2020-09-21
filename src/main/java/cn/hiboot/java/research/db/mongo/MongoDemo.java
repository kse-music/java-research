package cn.hiboot.java.research.db.mongo;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.CursorType;
import com.mongodb.client.*;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.IndexOptions;
import org.bson.BsonTimestamp;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author DingHao
 * @since 2020/9/21 22:01
 */
public class MongoDemo extends MongoTest {

    private BsonTimestamp queryTs;

    @Test
    public void collection(){
        mongoDatabase.drop();

        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions();
//        mongoDatabase.createCollection("test");
        mongoDatabase.createCollection("test",createCollectionOptions);

        mongoDatabase.getCollection("test").drop();
    }

    @Test
    public void index(){
        IndexOptions indexOptions = new IndexOptions();
        indexOptions.background(true).name("index_demo");
        mongoDatabase.getCollection("test").createIndex(new Document("id",1),indexOptions);

        for (Document test : mongoDatabase.getCollection("test").listIndexes()) {
            System.out.println(test);
        }

        mongoDatabase.getCollection("test").dropIndex("index_demo");
    }

    public void opLogTest() {
        MongoCollection<Document> collection = mongoClient.getDatabase("local").getCollection("oplog.rs");

        //如果是首次订阅，需要使用自然排序查询，获取第最后一次操作的操作时间戳。如果是续订阅直接读取记录的值赋值给queryTs即可
        FindIterable<Document> tsCursor = collection.find().sort(new BasicDBObject("$natural", -1)).limit(1);
        Document tsDoc = tsCursor.first();
        queryTs =  tsDoc.get("ts",BsonTimestamp.class);
        while (true) {
            try {
                //构建查询语句,查询大于当前查询时间戳queryTs的记录
                BasicDBObject query = new BasicDBObject("ts", new BasicDBObject("$gt", queryTs));
                MongoCursor<Document> docCursor = collection.find(query)
                        .cursorType(CursorType.TailableAwait) //没有数据时阻塞休眠
                        .noCursorTimeout(true) //防止服务器在不活动时间（10分钟）后使空闲的游标超时。
                        .oplogReplay(true) //结合query条件，获取增量数据，这个参数比较难懂，见：https://docs.mongodb.com/manual/reference/command/find/index.html
                        .maxAwaitTime(1, TimeUnit.SECONDS) //设置此操作在服务器上的最大等待执行时间
                        .iterator();
                while (docCursor.hasNext()) {
                    Document document = docCursor.next();
                    //更新查询时间戳
                    queryTs = (BsonTimestamp) document.get("ts");
                    //在这里接收到数据后通过订阅数据路由分发

                    String op = document.getString("op");
                    String database = document.getString("ns");
                    Document context = (Document) document.get("o");
                    Document where = null;
                    if (op.equals("u")) {
                        where = (Document) document.get("o2");
                        if (context != null) {
                            context = (Document) context.get("$set");
                        }
                    }
                    System.err.println("操作时间戳：" + queryTs.getTime());
                    System.err.println("操作类  型：" + op);
                    System.err.println("数据库.集合：" + database);
                    System.err.println("更新条件：" + JSON.toJSONString(where));
                    System.err.println("文档内容：" + JSON.toJSONString(context));
                }
            } catch (Exception e) { e.printStackTrace(); }
        }

    }

    @Test
    public void tx(){
        //多文档事务执行的时候,不会自动创建命名空间
        MongoClient mongoClient = MongoHelper.mongoClient("192.168.4.30:19130,192.168.4.30:19131");
        mongoClient.getDatabase("tx").createCollection("foo");
        mongoClient.getDatabase("tx").createCollection("bar");
        try (ClientSession clientSession = mongoClient.startSession()){
            clientSession.startTransaction();
            MongoCollection<Document> coll1 = mongoClient.getDatabase("tx").getCollection("foo");
            MongoCollection<Document> coll2 = mongoClient.getDatabase("tx").getCollection("bar");
            coll1.insertOne(clientSession, new Document("abc", 1));
            System.out.println(1/0);
            coll2.insertOne(clientSession, new Document("xyz", 999));
            clientSession.commitTransaction();
        }
    }


    @Test
    public void getFields(){
        for (String name : mongoClient.getDatabase("kg_attribute_definition").listCollectionNames()) {
            if(!name.contains("kg_ct_attribute")){
                mongoClient.getDatabase("kg_attribute_definition").getCollection(name).drop();
            }
        }
    }

    @Test
    public void mapReduce(){
		MongoCollection<Document> table = mongoClient.getDatabase("test_db").getCollection("test_coll");
		String map = "function() {for (var key in this) { emit(key, null); } }";
		String reduce = "function(key, stuff) { return null; }";
		MapReduceIterable<Document> docs = table.mapReduce(map, reduce);
		for (Document document : docs) {
			System.out.println(document.get("_id"));
		}
    }


}
