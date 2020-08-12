package cn.hiboot.java.research.db.redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 消息生成者
 */
public class Publish {
    public final static String QUEUE_NAME="rabbitMQ.test";
    
    private JedisPool jedisPool = jedisPool();
    
    @Test
    public void publish() throws InterruptedException{
    	Jedis jedis = jedisPool.getResource();
    	jedis.publish(QUEUE_NAME, "redis发布订阅");
    	jedis.close();
    }

    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        return new JedisPool(config, "",6379,3000);
    }

}
