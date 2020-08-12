package cn.hiboot.java.research.db.es;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AggregationDemo {

    private static final String ES_DB_NAME = "twitter";
    private static final long ES_TIMEOUT = 5L;
    private static float DEFAULT_MIN_SCORE = 0.05f;

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
    public void query() throws IOException {
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.should(QueryBuilders.termQuery("dateApplication", "2009.03.24"));

		query.filter(QueryBuilders.termQuery("dateApplication", "2009.03.24"));

//		AggregationBuilder by_key = AggregationBuilders.filters("by_key",query);
        AggregationBuilder by_key = AggregationBuilders.filter("by_key2", query);
//		AggregationBuilder by_key = AggregationBuilders.terms("terms_ag").field("holder");

        //The boost is applied only for term queries (prefix, range and fuzzy queries are not boosted).
		/*
		 * 警告：性能考量
			只有当你需要对搜索结果和聚合使用不同的过滤方式时才考虑使用post_filter。有时一些用户会直接在常规搜索中使用post_filter。
			不要这样做！post_filter会在查询之后才会被执行，因此会失去过滤在性能上帮助(比如缓存)。
			post_filter应该只和聚合一起使用，并且仅当你使用了不同的过滤条件时。
		 */

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(query)
                .aggregation(by_key)
//                .postFilter()//后置过滤器，不参与查询
                .minScore(DEFAULT_MIN_SCORE)
                .timeout(TimeValue.timeValueSeconds(ES_TIMEOUT))
                .size(0);

        SearchRequest searchRequest = new SearchRequest(ES_DB_NAME)
                .source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        System.out.println(response);

        Filters agg = response.getAggregations().get("by_key");
        for (Filters.Bucket entry : agg.getBuckets()) {
            String key = entry.getKeyAsString();
            long docCount = entry.getDocCount();
            System.out.println(key + "---" + docCount);
        }

        Filter agg2 = response.getAggregations().get("by_key2");
        System.out.println(agg2.getName() + agg2.getDocCount());

        Terms genders = response.getAggregations().get("terms_ag");
        for (Terms.Bucket entry : genders.getBuckets()) {
            System.out.println(entry.getKey() + "---" + entry.getDocCount());
        }
    }

    @Test
    public void testDateH() throws IOException {
        AggregationBuilder by_date = AggregationBuilders.dateRange("date_range").field("publishTime")
                //				.addRange(getPrevDay(-2), getPrevDay(-1))
                //				.addRange(getPrevDay(-3), getPrevDay(-2))
                //				.addRange(getPrevDay(-4), getPrevDay(-3))
                //				.addRange(getPrevDay(-5), getPrevDay(-4))
                //				.addRange(getPrevDay(-6), getPrevDay(-5))
                //				.addRange(getPrevDay(-7), getPrevDay(-6))
                .addRange(getPrevDay(-145), getPrevDay(-143));

        //		AggregationBuilder by_date =
        //				AggregationBuilders
        //				.dateHistogram("agg")
        //				.field("publishTime")
        //				.dateHistogramInterval(DateHistogramInterval.DAY);
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        String start = getPrevDay(-145);
        String end = getPrevDay(-143);
        bool.must(QueryBuilders.rangeQuery("publishTime").gt(start).lt(end));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(bool)
                .aggregation(by_date)
//                .postFilter()//后置过滤器，不参与查询
                .minScore(DEFAULT_MIN_SCORE)
                .timeout(TimeValue.timeValueSeconds(ES_TIMEOUT))
                .size(0);

        SearchRequest searchRequest = new SearchRequest("u260")
                .source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(response);

        Range agg = response.getAggregations().get("date_range");
        for (Range.Bucket entry : agg.getBuckets()) {
            String key = entry.getKeyAsString();                // Date range as key
            DateTime fromAsDate = (DateTime) entry.getFrom();   // Date bucket from as a Date
            DateTime toAsDate = (DateTime) entry.getTo();       // Date bucket to as a Date
            long docCount = entry.getDocCount();                // Doc count
            System.out.println(key);
            System.out.println(fromAsDate);
            System.out.println(toAsDate);
            System.out.println(docCount);
        }

        //		Histogram  agg = response.getAggregations().get("agg");
        //		for (Histogram .Bucket entry : agg.getBuckets()) {
        //			String date = entry.getKeyAsString();
        //			Long num = entry.getDocCount();
        //		}

    }

    private String getPrevDay(int day) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, day + 2);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(now);
    }

}
