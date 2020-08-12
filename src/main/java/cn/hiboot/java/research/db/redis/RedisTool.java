package cn.hiboot.java.research.db.redis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁
 *
 * @author DingHao
 * @since 2019/5/9 17:33
 */
public class RedisTool {

    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 5s 锁的超时时间
     */
    private static final long DEFAULT_WAIT_LOCK_TIME_OUT = 5;

    private JedisPool jedisPool = jedisPool();

    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        SetParams setParams = new SetParams().px(expireTime).nx();
        String result = jedis.set(lockKey, requestId, setParams);
        long nanoWaitForLock = TimeUnit.SECONDS.toNanos(DEFAULT_WAIT_LOCK_TIME_OUT);
        long start = System.nanoTime();
        while ((System.nanoTime() - start < nanoWaitForLock) && !LOCK_SUCCESS.equals(result)) {
            result = jedis.set(lockKey, requestId, setParams);
            if(LOCK_SUCCESS.equals(result)){
                return true;
            }
            try {
                //加随机时间防止活锁
                TimeUnit.MILLISECONDS.sleep(1000 + new Random().nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return LOCK_SUCCESS.equals(result);
    }

    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if(RELEASE_SUCCESS.equals(result)){
            jedis.close();
        }
        return RELEASE_SUCCESS.equals(result);
    }

    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        return new JedisPool(config, "192.168.1.159",6379,3000);
    }

    public Jedis jedis(){
        return jedisPool.getResource();
    }

    @Test
    public void lock(){
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                Jedis jedis = jedis();
                if (tryGetDistributedLock(jedis,"lock_name","lock",5000)){
                    System.out.println(Thread.currentThread().getName() + " | " + System.currentTimeMillis());
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(releaseDistributedLock(jedis,"lock_name","lock"));
                }else {
                    System.out.println(Thread.currentThread().getName() + "没拿到");
                }
            }).start();
        }
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
