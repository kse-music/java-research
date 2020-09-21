package cn.hiboot.java.research.db.mongo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertManyOptions;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class ExtendDemo extends MongoTest{

    @Test
    public void test() {
        MongoCollection<Document> collection = mongoClient.getDatabase("test").getCollection("flat2");
        collection.createIndex(new Document("id", 1));
        collection.createIndex(new Document("concept_ids", 1));
        List<Document> list = Lists.newArrayListWithCapacity(100000);
        InsertManyOptions insertManyOptions = new InsertManyOptions().ordered(false).bypassDocumentValidation(false);
        long s = System.currentTimeMillis();
        int insertCount = 10000000;
        //插入一条数据后，内存占用104byte,存储大小4KB，索引数4，索引大小28KB
        //new Document("entity_type",1).append("entity_id",3).append("attr_id",1).append("attr_value_type",2).append("attr_value",5)
        for (int i = 1; i <= insertCount; i++) {
            list.add(new Document("id", i).append("concept_ids", Lists.newArrayList(randomConcept(), randomConcept())));
            if (list.size() % 100000 == 0) {
                collection.insertMany(list, insertManyOptions);
                list.clear();
            }
        }
        if (list.size() > 0) {
            collection.insertMany(list, insertManyOptions);
        }
        System.out.println(System.currentTimeMillis() - s);
    }

    private Long randomConcept() {
        Random random = new Random();
        return Integer.valueOf(random.nextInt(1000)).longValue();
    }

    @Test
    public void kg() {
        BasicParam basicParam = new BasicParam();
        basicParam.setConceptId(1L);
        basicParam.setName("胡雪岩");
//        basicParam.setMeaningTag("hxy");
        Map<String, String> privateData = Maps.newHashMap();
        privateData.put("嘿嘿", "123");
        basicParam.setPrivateAttributes(privateData);
        Map<Integer, String> attData = Maps.newHashMap();
        attData.put(1, "30");
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
    public void excel() throws IOException {
        MongoCollection<Document> col = mongoClient.getDatabase("patent_kg_b").getCollection("patent_info");
        MongoCursor<Document> cursor = col.find().limit(200).iterator();
        int count = 0;
        Set<String> dic = new HashSet<>();
        try(FileWriter fw = new FileWriter("data/doc.txt"); MongoCursor<Document> dcursor = col.find().limit(1).iterator()){
            while (dcursor.hasNext()) {
                Document doc = dcursor.next();
                dic.addAll(doc.keySet());
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

    @Setter
    @Getter
    static class BasicParam {

        private Long id;
        private Long conceptId;

        private Integer type;

        private String name;

        private String meaningTag;

        private String abs;
        private String imageUrl;

        private List<String> synonyms;
        private Map<Integer,String> attributes;
        private Map<String,String> privateAttributes;
    }
}
