package cn.hiboot.java.research.db.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class LowRestClientDemo {

    private static final String INDEX = "twitter";

    private RestClient client;

    @BeforeEach
    public void init(){
        client = EsClient.lowClient();
    }

    @AfterEach
    public void end(){
        if(client != null){
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void lowClient() throws IOException {
        //RestClient是线程安全的
        HttpEntity entity = new NStringEntity("{\"query\":{\"bool\":{\"must\":[{\"query_string\":{\"default_field\":\"name\",\"query\":\"机器人\"}},{\"term\":{\"type\":\"发明专利\"}},{\"range\":{\"dateApplication\":{\"gt\":\"2016.01.01\",\"lt\":\"2016.12.31\"}}}],\"must_not\":[],\"should\":[]}},\"size\":0,\"_source\":{\"excludes\":[]},\"aggs\":{\"2\":{\"date_histogram\":{\"field\":\"datePublication\",\"interval\":\"1M\",\"min_doc_count\":0,\"extended_bounds\":{\"min\":\"2016.01.01\",\"max\":\"2016.12.31\"}}}}}", ContentType.APPLICATION_JSON);
        Request request = new Request("GET", "/patent/_search");
        request.setEntity(entity);
        Response response = client.performRequest(request);
        System.out.println(EntityUtils.toString(response.getEntity()));
    }


    @Test
    public void scroll() throws Exception {
        Request request = new Request(HttpGet.METHOD_NAME, "/" + INDEX + "/_search");
        HttpEntity entity = new NStringEntity("{\"query\": {\"match_all\":{}},\"size\": 10000}", ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        Response response = client.performRequest(request);
        String data = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSON.parseObject(data);
        String scrollId = jsonObject.getString("_scroll_id");
        JSONArray jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");
        //把导出的结果以JSON的格式写到文件里
        File file = new File("src/docs/data/es.txt");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
        for (int i = 0; i < jsonArray.size(); i++) {
            String json = jsonArray.getString(i);
            out.write(json);
            out.write("\r\n");
        }
        try {
            while (!jsonArray.isEmpty()) {
                entity = new NStringEntity("{\"scroll\" : \"1m\",\"scroll_id\" : \"" + scrollId + "\"}", ContentType.APPLICATION_JSON);
                request = new Request(HttpPost.METHOD_NAME, "/_search/scroll");
                request.setEntity(entity);
                response = client.performRequest(request);
                data = EntityUtils.toString(response.getEntity());
                jsonObject = JSON.parseObject(data);
                jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");
                for (int i = 0; i < jsonArray.size(); i++) {
                    String json = jsonArray.getString(i);
                    out.write(json);
                    out.write("\r\n");
                }
//                entity = new NStringEntity("{\"scroll_id\" : [\""+scrollId+"]\"}", ContentType.APPLICATION_JSON);
//                response = restClient.performRequest(HttpDelete.METHOD_NAME, "/_search/scroll/"+scrollId, Collections.emptyMap());
//                response = restClient.performRequest(HttpDelete.METHOD_NAME, "/_search/scroll", Collections.emptyMap(), entity);
            }
            log.info("查询结束");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }

    }

    @Test
    public void syncRestClient() throws IOException {
        int numRequests = 10;
        final CountDownLatch latch = new CountDownLatch(numRequests);
        HttpEntity[] entities = new HttpEntity[numRequests];
        for (int i = 0; i < numRequests; i++) {
            Request request = new Request("PUT", "/twitter/" + i);
            request.setEntity(entities[i]);
            client.performRequestAsync(
                    request,
                    new ResponseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            System.out.println(response);
                            latch.countDown();
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            log.error("{}",1);
                            latch.countDown();
                        }
                    }
            );
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
