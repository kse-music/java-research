package cn.hiboot.java.research.db.es;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class IndexDemo {

    private static final String INDEX = "twitter";

    private RestHighLevelClient client;

    @BeforeEach
    public void init(){
        client = EsClient.highClient();
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
    public void create() {
        CreateIndexRequest request = new CreateIndexRequest(INDEX);
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 1)
        );
        request.alias(new Alias("tw"));

//        request.settings(loadJson("data/settings.json"), XContentType.JSON);//两步到位
//        request.mapping(loadJson("data/mapping.json"), XContentType.JSON);

        request.source(loadJson("data/index.json"), XContentType.JSON);//一步到位

        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("result = {}", createIndexResponse.isAcknowledged());
    }

    @Test
    public void putMapping() {
        PutMappingRequest request = new PutMappingRequest(INDEX);
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.startObject("properties");
                {
                    builder.startObject("message");
                    {
                        builder.field("type", "text");
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
            request.source(builder);
            AcknowledgedResponse putMappingResponse = client.indices().putMapping(request, RequestOptions.DEFAULT);
            log.info("result = {}", putMappingResponse.isAcknowledged());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean indexExist() {
        GetIndexRequest request = new GetIndexRequest(INDEX);
        boolean exists = false;
        try {
            exists = client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    @Test
    public void delete() {
        if (indexExist()) {
            DeleteIndexRequest request = new DeleteIndexRequest(INDEX);
            try {
                AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
                log.info("result = {}", deleteIndexResponse.isAcknowledged());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void modify() {
        UpdateSettingsRequest request = new UpdateSettingsRequest(INDEX);
        request.settings(Settings.builder()
//                .put("index.number_of_shards", 5)
                .put("index.number_of_replicas", 1)
        );
        try {
            AcknowledgedResponse updateSettingsResponse = client.indices().putSettings(request, RequestOptions.DEFAULT);
            log.info("result = {}", updateSettingsResponse.isAcknowledged());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getSetting() {
        GetSettingsRequest request = new GetSettingsRequest().indices(INDEX);
        try {
            GetSettingsResponse response = client.indices().getSettings(request, RequestOptions.DEFAULT);
            for (ObjectObjectCursor<String, Settings> cursor : response.getIndexToSettings()) {
                String index = cursor.key;
                Settings settings = cursor.value;
                Integer shards = settings.getAsInt("index.number_of_shards", null);
                Integer replicas = settings.getAsInt("index.number_of_replicas", null);
                log.info(index);
                log.info("result = {}", settings);
                log.info("result = {}", shards);
                log.info("result = {}", replicas);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void refresh() {
        RefreshRequest request = new RefreshRequest(INDEX);
        try {
            RefreshResponse refreshResponse = client.indices().refresh(request, RequestOptions.DEFAULT);
            log.info("result = {}", refreshResponse.isFragment());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadJson(String file) {
        String str = "";
        try {
            str = Files.readAllLines(new File(file).toPath()).stream().reduce((s1, s2) -> s1 + s2).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

}
