package cn.hiboot.java.research.db.mongo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.CursorType;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonTimestamp;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class MongoDemo {

    private MongoClient mongoClient = MongoHelper.mongoClient("192.168.4.30",19130);

    @Test
    public void bulk(){
        BasicParam basicParam = new BasicParam();
        basicParam.setConceptId(1L);
        basicParam.setName("胡雪岩");
//        basicParam.setMeaningTag("hxy");
        Map<String,String> privateData = Maps.newHashMap();
        privateData.put("嘿嘿","123");
        basicParam.setPrivateAttributes(privateData);
        Map<Integer,String> attData = Maps.newHashMap();
        attData.put(1,"30");
        basicParam.setAttributes(attData);
        basicParam.setSynonyms(Lists.newArrayList("HuXY"));
        basicParam.setType(1);
        List<BasicParam> objects = Lists.newArrayListWithCapacity(1000000);
        for (int i = 0; i < 1000000; i++) {
            objects.add(basicParam);
        }

//        RestTemplate restTemplate = new RestTemplate();
//        long s = System.currentTimeMillis();
//        restTemplate.postForObject("http://127.0.0.1:9887/edit/batch/entities/add?kgName={1}", objects, String.class, "string");
//        System.out.println(System.currentTimeMillis()-s);

    }

    @Test
    public void basic() {
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
        //创建集合
        mongoDatabase.getCollection("test").drop();
        mongoDatabase.createCollection("test");

        //获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("test");

        //插入数据
        Document document = new Document("title", "MongoDB").
                append("description", "database").
                append("likes", 100).
                append("by", "Fly");
        List<Document> documents = new ArrayList<>();

        documents.add(document);

        collection.insertMany(documents);

        //创建索引
        collection.createIndex(new Document("likes",1).append("by",1),new IndexOptions().name("lb"));

        //检索所有文档
        /**
         * 1. 获取迭代器FindIterable<Document>
         * 2. 获取游标MongoCursor<Document>
         * 3. 通过游标遍历检索出的文档集合
         * */
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while(mongoCursor.hasNext()){
            Document next = mongoCursor.next();
            System.out.println(next);
//            collection.deleteOne(Filters.eq(next.get("_id").toString()));
        }

        //更新文档
        collection.updateMany(Filters.eq("likes", 100), Updates.set("likes",200));

        //删除符合条件的第一个文档
        collection.deleteOne(Filters.eq("likes", 200));

        //删除所有符合条件的文档
        collection.deleteMany (Filters.eq("likes", 200));
    }

    @Test
    public void senior() {
        MongoCollection<Document> collection = mongoClient.getDatabase("test").getCollection("test");
        Document data = new Document("title", "MongoDB").
                append("description", "database").
                append("likes", 100).
                append("by", "Fly");

        InsertOneModel insertOneModel = new InsertOneModel<>(data);

        UpdateOneModel updateOneModel = new UpdateOneModel<>(new Document("likes",100),new Document("$set",new Document("likes",200)),new UpdateOptions().upsert(true));
        UpdateManyModel updateManyModel = new UpdateManyModel<>(new Document("likes",100),new Document("$set",new Document("likes",200)),new UpdateOptions().upsert(true));

        ReplaceOneModel replaceOneModel = new ReplaceOneModel<>(new Document("likes",200),new Document("likes",20000),new ReplaceOptions().upsert(true));

        DeleteOneModel deleteOneModel = new DeleteOneModel<>(new Document("likes",20000));
        DeleteManyModel deleteManyModel = new DeleteManyModel<>(new Document("likes",20000));

        List<WriteModel<Document>> requests = Lists.newArrayList(insertOneModel,updateOneModel,replaceOneModel,deleteOneModel);

        BulkWriteResult bulkWriteResult = collection.bulkWrite(requests);

        System.out.println(bulkWriteResult);
    }

    @Test
    public void xn(){
        MongoCollection<Document> collection = mongoClient.getDatabase("test").getCollection("flat2");
        collection.createIndex(new Document("id",1));
        collection.createIndex(new Document("concept_ids",1));
        List<Document> list = Lists.newArrayListWithCapacity(100000);
        InsertManyOptions insertManyOptions = new InsertManyOptions().ordered(false).bypassDocumentValidation(false);
        long s = System.currentTimeMillis();
        int insertCount = 10000000;
        //插入一条数据后，内存占用104byte,存储大小4KB，索引数4，索引大小28KB
        //new Document("entity_type",1).append("entity_id",3).append("attr_id",1).append("attr_value_type",2).append("attr_value",5)
        for (int i = 1; i <= insertCount; i++) {
            list.add(new Document("id",i).append("concept_ids", Lists.newArrayList(randomConcept(),randomConcept())));
            if(list.size() % 100000 == 0){
                collection.insertMany(list,insertManyOptions);
                list.clear();
            }
        }
        if(list.size() > 0){
            collection.insertMany(list,insertManyOptions);
        }
        System.out.println(System.currentTimeMillis()-s);
    }

    private Long randomConcept(){
        Random random = new Random();
        return Integer.valueOf(random.nextInt(1000)).longValue();
    }

    @Test
    public void find(){
//        for (Document document : mongoClient.getDatabase("test").getCollection("entity").listIndexes()) {
//            System.out.println(document);
//        }

        pushToArray();
        deleteFromArray();
    }

    private BsonTimestamp queryTs;

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

    /**
     * 从数组集合中删除一个元素
     */
    private void pushToArray(){
        UpdateResult updateResult = mongoClient.getDatabase("test").getCollection("basic").updateOne(new Document("id", 1L), Updates.push("ids", 123));
        System.out.println(updateResult);
    }

    /**
     * 从数组集合中删除一个元素
     */
    private void deleteFromArray(){
        UpdateResult updateResult = mongoClient.getDatabase("test").getCollection("basic").updateOne(new Document("id", 1L), Updates.pull("ids", new Document("$gt", 1L)));
        System.out.println(updateResult);
    }


    private void setDefault(String db,int t,MongoClient mongoClient){
        List<UpdateManyModel<Document>> requests = new ArrayList<>();
        String table;
        Document filter = new Document();
        Document projection;
        if(t == 0){//实体
            table = "basic_info";
            filter.append("type",1);
            projection = new Document("id", 1).append("meta_data", 1).append("_id", 0);
        }else if(t == 1){//关系
            table = "attribute_object";
            projection = new Document("meta_data",1);
        }else{//私有关系
            table = "attribute_private_object";
            projection = new Document("meta_data",1);
        }

        FindIterable<Document> documents = mongoClient.getDatabase(db).getCollection(table).find(filter).projection(projection);

        for (Document document : documents) {
            if(t == 0){
                if(document.getLong("id") == 0){
                    continue;
                }
            }
            Document meta = document.get("meta_data", Document.class);
            if(meta == null){
                continue;
            }
            boolean flag = true;
            if(!meta.containsKey("meta_data_3")){
                meta.put("meta_data_3",0d);
                flag = false;
            }
            if(!meta.containsKey("meta_data_12")){
                meta.put("meta_data_12",0d);
                flag = false;
            }
            if(flag){
                continue;
            }
            UpdateManyModel<Document> updateManyModel ;
            if(t == 0){
                updateManyModel = new UpdateManyModel<>(new Document("id",document.getLong("id")),new Document("$set",new Document("meta_data",meta)));
            }else {
                updateManyModel = new UpdateManyModel<>(new Document("_id",document.getObjectId("_id")),new Document("$set",new Document("meta_data",meta)));
            }
            requests.add(updateManyModel);
            if(requests.size() % 10000 == 0){
                mongoClient.getDatabase(db).getCollection(table).bulkWrite(requests);
                requests.clear();
            }
        }
        if(!requests.isEmpty()){
            mongoClient.getDatabase(db).getCollection(table).bulkWrite(requests);
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
    public void downloadImg(){
        MongoCollection<Document> col = mongoClient.getDatabase("kg_cbnode_c").getCollection("concept_instance");
        Consumer<Document> printer = t -> {
            Long id = t.getLong("ins_id");
            downloadPic("http://7xveaq.com1.z0.glb.clouddn.com/"+id+".jpg", id+".jpg","data/image/");
        };
        col.find(Filters.eq("concept_id", 13)).forEach(printer);
    }

    public void downloadPic(String imgUrl,String fileName,String filePath) {
        byte[] btImg = getImageFromNetByUrl(imgUrl);
        if ( null != btImg && btImg.length > 0 ){
            System.out.println("read: " + btImg.length + " byte");
            writeImageToDisk(btImg, fileName,filePath);
        } else {
            System.out.println("没有从该连接获得内容");
        }
    }
    /**
     * 将图片写入到磁盘
     * @param img 图片数据流
     * @param fileName 文件保存时的名称
     */
    public static void writeImageToDisk(byte[] img, String fileName,String filePath) {
        try {
            FileOutputStream fops = new FileOutputStream(filePath + fileName);
            fops.write(img);
            fops.flush();
            fops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据地址获得数据的字节流
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xstream,application/xstream;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Cookie", "gr_user_id=8b976033-64fe-4861-8ff7-099b7fe55b46; pgv_pvi=770369536; OUTFOX_SEARCH_USER_ID_NCOO=563651296.908239; identity=lichunxiao%40hiekn.com; remember_code=PtCsn%2FqcL1; session=22b1d78a4c54db58e46cd76f12b5de95398d6d07; _gat=1; gr_session_id_eee5a46c52000d401f969f4535bdaa78=86bd95dd-474c-4be1-890f-01a052c20ebd; Hm_lvt_1c587ad486cdb6b962e94fc2002edf89=1477548670,1477552076,1477552410,1477982450; Hm_lpvt_1c587ad486cdb6b962e94fc2002edf89=1478072258; _ga=GA1.2.541892866.1466752277");
            conn.setRequestProperty("Host", "cdn.itjuzi.com");
            conn.setRequestProperty("Proxy-Connection", "keep-alive");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("If-None-Match", "580828a5-8e0");
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 从输入流中获取数据
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len=inStream.read(buffer)) != -1 ) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    @Test
    public void getFields(){
//		MongoCollection<Document> table = mongoClient.getDatabase("test_db").getCollection("test_coll");
//		String map = "function() {for (var key in this) { emit(key, null); } }";
//		String reduce = "function(key, stuff) { return null; }";
//		MapReduceIterable<Document> docs = table.mapReduce(map, reduce);
//		for (Document document : docs) {
//			System.out.println(document.get("_id"));
//		}
        for (String name : mongoClient.getDatabase("kg_attribute_definition").listCollectionNames()) {
            if(!name.contains("kg_ct_attribute")){
                mongoClient.getDatabase("kg_attribute_definition").getCollection(name).drop();
            }
        }
    }

    @Test
    public void excel() throws IOException {
        MongoCollection<Document> col = mongoClient.getDatabase("patent_kg_b").getCollection("patent_info");
        MongoCursor<Document> cursor = col.find().limit(200).iterator();
        int count = 0;
        Set<String> dic = new HashSet<>();
        try(FileWriter fw = new FileWriter("data/doc.txt");MongoCursor<Document> dcursor = col.find().limit(1).iterator()){
            while (dcursor.hasNext()) {
                Document doc = dcursor.next();
                for (String string : doc.keySet()) {
                    dic.add(string);
                }
                dic.add("agencyOrg");
                dic.add("agencyPerson");
            }
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                System.out.println(doc.toJson());
                String title = "";
                String result = "";
                for (String key : dic) {
                    title = title + key + "@";
                    String s = doc.get(key) == null ? "" : doc.get(key).toString();
                    result = result + s + "@";
                }
                if (count == 0) {
                    fw.write(title + "\r\n");
                    fw.flush();
                }
                fw.write(result + "\r\n");
                fw.flush();
                count++;
            }
        }
    }
}
