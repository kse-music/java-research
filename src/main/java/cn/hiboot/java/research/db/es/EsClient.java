package cn.hiboot.java.research.db.es;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.Iterator;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2019/4/25 0:53
 */
public class EsClient {

    private static final String DEFAULT_IP = "192.168.4.30";

    public static RestClient lowClient() {
        return builder(createHttpHost(DEFAULT_IP, 9200),createHttpHost(DEFAULT_IP, 19200)).build();
    }

    public static RestHighLevelClient highClient() {
        return new RestHighLevelClient(builder(createHttpHost(DEFAULT_IP, 9200),createHttpHost(DEFAULT_IP, 19200)));
    }

    public static RestClient lowClient(String ip, Integer port, String schema) {
        return builder(new HttpHost(ip, port,schema)).build();
    }

    public static RestHighLevelClient highClient(String ip, Integer port, String schema) {
        return new RestHighLevelClient(builder(new HttpHost(ip, port,schema)));
    }

    private static HttpHost createHttpHost(String ip,int port){
        return new HttpHost(ip, port);
    }

    private static RestClientBuilder builder(HttpHost... httpHosts) {
        RestClientBuilder builder = RestClient.builder(httpHosts);
        Header[] defaultHeaders = new Header[]{new BasicHeader("x", "y")};
        builder.setDefaultHeaders(defaultHeaders);
        builder.setRequestConfigCallback((RequestConfig.Builder requestConfigBuilder) -> requestConfigBuilder.setConnectTimeout(5000).setSocketTimeout(60000));
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("user", "password"));
        builder.setHttpClientConfigCallback((HttpAsyncClientBuilder httpAsyncClientBuilder) ->
                httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        builder.setNodeSelector((Iterable<Node> nodes) -> {
            boolean foundOne = false;
            for (Node node : nodes) {
//                String rackId = node.getAttributes().get("rack_id").get(0);
//                if ("rack_one".equals(rackId)) {
//                    foundOne = true;
//                    break;
//                }
            }
            if (foundOne) {
                Iterator<Node> nodesIt = nodes.iterator();
                while (nodesIt.hasNext()) {
                    Node node = nodesIt.next();
                    String rackId = node.getAttributes().get("rack_id").get(0);
                    if ("rack_one".equals(rackId) == false) {
                        nodesIt.remove();
                    }
                }
            }
        });
        return builder;
    }

}
