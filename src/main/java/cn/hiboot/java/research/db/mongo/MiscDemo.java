package cn.hiboot.java.research.db.mongo;

import com.google.common.collect.Lists;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/11/4 23:32
 */
public class MiscDemo extends MongoTest{

    @Test
    public void find(){

    }

    @Test
    public void senior() {
        pushToArray();
        deleteFromArray();
    }


    /**
     * 从数组集合中删除一个元素
     */
    private void pushToArray(){
        MongoCollection<Document> basic = mongoDatabase.getCollection("basic");
        UpdateResult updateResult = basic.updateOne(new Document("id", 1L), Updates.push("ids", 123));
        System.out.println(updateResult);
    }

    /**
     * 从数组集合中删除一个元素
     */
    private void deleteFromArray(){
        MongoCollection<Document> basic = mongoDatabase.getCollection("basic");
        UpdateResult updateResult = basic.updateOne(new Document("id", 1L), Updates.pull("ids", new Document("$gt", 1L)));
        System.out.println(updateResult);
    }


    @Test
    public void agg(){
        MongoCollection<Document> collection = mongoClient.getDatabase("wuyue2331").getCollection("paper");
        List<Bson> list = Lists.newArrayList();
        list.add(Aggregates.project(new Document("mk","$_metaData._knowledgeIds").append("_id",0)));
        list.add(Aggregates.unwind("$mk"));
        list.add(Aggregates.match(new Document("mk",new Document("$in", Lists.newArrayList(19,110,147,277,378,433,532,652,736)))));
        list.add(Aggregates.group(new Document("_id","$mk"),new BsonField("count",new Document("$sum",1))));

        AggregateIterable<Document> aggregate = collection.aggregate(list);
        for (Document document : aggregate) {
            System.out.println(document);
        }
    }

    @Test
    public void basic() {
        collection.insertOne(new Document("title", "MongoDB").
                append("description", "database").
                append("likes", 100).
                append("by", "Fly"));

        for (Document next : collection.find()) {
            System.out.println(next);
        }

        //更新文档
        collection.updateMany(Filters.eq("likes", 100), Updates.set("likes",200));

        //删除符合条件的第一个文档
        collection.deleteOne(Filters.eq("likes", 200));

        //删除所有符合条件的文档
        collection.deleteMany(Filters.eq("likes", 200));
    }

    @Test
    public void bulk() {
        Document data = new Document("title", "MongoDB").
                append("description", "database").
                append("likes", 100).
                append("by", "Fly");

        InsertOneModel insertOneModel = new InsertOneModel<>(data);

        UpdateOneModel updateOneModel = new UpdateOneModel<>(new Document("likes", 100), new Document("$set", new Document("likes", 200)), new UpdateOptions().upsert(true));
        UpdateManyModel updateManyModel = new UpdateManyModel<>(new Document("likes", 100), new Document("$set", new Document("likes", 200)), new UpdateOptions().upsert(true));

        ReplaceOneModel replaceOneModel = new ReplaceOneModel<>(new Document("likes", 200), new Document("likes", 20000), new ReplaceOptions().upsert(true));

        DeleteOneModel deleteOneModel = new DeleteOneModel<>(new Document("likes", 20000));
        DeleteManyModel deleteManyModel = new DeleteManyModel<>(new Document("likes", 20000));

        List<WriteModel<Document>> requests = Lists.newArrayList(insertOneModel, updateOneModel, replaceOneModel, deleteOneModel);

        BulkWriteResult bulkWriteResult = collection.bulkWrite(requests);

        System.out.println(bulkWriteResult);
    }
}
