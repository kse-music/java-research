package cn.hiboot.java.research.db.es;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

@Slf4j
public class SearchDemo {

    private static final String INDEX = "twitter";

    private RestHighLevelClient client;

    @BeforeEach
    public void init() {
        client = EsClient.highClient();
    }

    @AfterEach
    public void end() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void search() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
//                .storedField("name")//不推荐，用source过滤
                .query(QueryBuilders.termQuery("name", "空调"))
                .explain(true)
//                .fetchSource(new String[]{"name"}, new String[]{})
                ;
        SearchRequest searchRequest = new SearchRequest(INDEX).source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        searchResponse.getHits().forEach(hit -> log.info(hit.getSourceAsString()));
        log.info("result = {}", searchResponse);
    }

    @Test
    public void function() throws IOException {
        ScoreFunctionBuilder<ExponentialDecayFunctionBuilder> exp = ScoreFunctionBuilders.exponentialDecayFunction("count", 0, 100, 0, 0.5).setWeight(0.2f);
        ScoreFunctionBuilder<LinearDecayFunctionBuilder> line = ScoreFunctionBuilders.linearDecayFunction("coefficient", 0, 10, 0, 0.5).setWeight(1f);

        FilterFunctionBuilder[] functions = {
                new FilterFunctionBuilder(exp),
                new FilterFunctionBuilder(line)
        };

        BoolQueryBuilder bool = QueryBuilders.boolQuery();

        FunctionScoreQueryBuilder s = QueryBuilders.functionScoreQuery(bool, functions).boostMode(CombineFunction.SUM);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(s)
                .explain(true);
        SearchRequest searchRequest = new SearchRequest().source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        log.info("result = {}", searchResponse);

    }

    @Test
    public void scroll() throws Exception {
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.matchAllQuery())
                .size(10000);

        SearchRequest searchRequest = new SearchRequest(INDEX)
                .source(searchSourceBuilder)
                .scroll(scroll);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        String scrollId = searchResponse.getScrollId();
        SearchHit[] searchHits = searchResponse.getHits().getHits();

        //把导出的结果以JSON的格式写到文件里
        File file = new File("G:/data/es.txt");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file, true));

        SearchScrollRequest scrollRequest = new SearchScrollRequest().scroll(scroll);
        while (searchHits != null && searchHits.length > 0) {
            for (SearchHit searchHit : searchHits) {
                out.write(searchHit.getSourceAsString());
                out.write("\r\n");
            }
            scrollRequest.scrollId(scrollId);
            searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = searchResponse.getScrollId();
            searchHits = searchResponse.getHits().getHits();
        }
        out.close();
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        boolean succeeded = clearScrollResponse.isSucceeded();

    }

    @Test
    public void dataOut() throws IOException {
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.matchAllQuery())
                .size(10000);

        SearchRequest searchRequest = new SearchRequest(INDEX)
                .source(searchSourceBuilder)
                .scroll(scroll)
                .searchType(SearchType.DEFAULT);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        String scrollId = searchResponse.getScrollId();

        BufferedWriter out = new BufferedWriter(new FileWriter("data/data.txt", true));

        SearchScrollRequest scrollRequest = new SearchScrollRequest().scroll(scroll);

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        while (searchHits != null && searchHits.length > 0) {
            log.info("查询数量 ：" + searchHits.length);
            for (SearchHit searchHit : searchHits) {
                out.write(searchHit.getSourceAsString());
                out.write("\r\n");
            }
            scrollRequest.scrollId(scrollId);
            searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
            scrollId = searchResponse.getScrollId();
            searchHits = searchResponse.getHits().getHits();
        }
        log.info("查询结束");
        out.close();
    }

    @Test
    public void dataIn() throws IOException {
        //读取刚才导出的ES数据
        try(BufferedReader br = new BufferedReader(new FileReader("data/data.txt"))){
            //开启批量插入
            BulkRequest request = new BulkRequest().timeout(TimeValue.timeValueMinutes(2));
            String json;
            while ((json = br.readLine()) != null) {
                request.add(new IndexRequest(INDEX).source(json, XContentType.JSON));
                //每一千条提交一次
                if (request.numberOfActions() % 1000 == 0) {
                    client.bulk(request, RequestOptions.DEFAULT);
                }
            }
            if (request.numberOfActions() != 0) {
                client.bulk(request, RequestOptions.DEFAULT);
            }
            log.info("插入完毕");
        }
    }


}
