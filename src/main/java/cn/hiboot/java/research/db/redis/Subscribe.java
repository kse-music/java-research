package cn.hiboot.java.research.db.redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

/**
 * 消费者
 * @author
 *
 */
public class Subscribe {
	
	public final static String QUEUE_NAME="rabbitMQ.test";

	private JedisPool jedisPool = jedisPool();

	@Test
	public void subscribe(){
		Jedis jedis = jedisPool.getResource();
		JedisPubSub jedisPubSub=new JedisPubSub() {
			@Override
			public void onMessage(String channel, String message) {
				System.out.println(message);
			}
		};
		jedis.subscribe(jedisPubSub, QUEUE_NAME);//subscribe是一个阻塞的方法,在取消订阅该频道前，会一直阻塞在这
		jedis.close();
	}

	public JedisPool jedisPool(){
		JedisPoolConfig config = new JedisPoolConfig();
		return new JedisPool(config, "",6379,3000);
	}
}