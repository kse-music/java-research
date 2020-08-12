package cn.hiboot.java.research.db.redis;

import org.junit.jupiter.api.Test;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.data.redis.connection.*;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2020/1/7 13:25
 */
public class RedisDemo {

    @Test
    public void find() {
//        String host = "192.168.4.16:6379,192.168.4.17:6379,192.168.4.18:6379";
//        String pwd = null;
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        RedisConnectionFactory redisConnectionFactory = getConnect(host,pwd,true) ;
//        context.registerBean(null,RedisConnectionFactory.class,() -> redisConnectionFactory);
//        context.registerBean(null,StringRedisTemplate.class,() -> new StringRedisTemplate(redisConnectionFactory));
//        context.refresh();
//        StringRedisTemplate bean = context.getBean(StringRedisTemplate.class);
//        Object basicId = bean.opsForHash().get("_kg.server:basic:bj735sbi_graph_16faca83f34", "basicId");
//        System.out.println(basicId);
    }

//    private RedisConnectionFactory getConnect(String host,String pwd,boolean isLettuce){
//        boolean single = true;
//        String[] split = host.split(",");
//        if(split.length > 1){
//            single = false;
//        }
//        RedisConnectionFactory redisConnectionFactory ;
//        if (single) {
//            String[] s = split[0].split(":");
//            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(s[0],Integer.parseInt(s[1]));
//            redisStandaloneConfiguration.setPassword(RedisPassword.of(pwd));
//            if(isLettuce){
//                redisConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
//            }else {
//                redisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
//            }
//        } else {
//            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
//            redisClusterConfiguration.setClusterNodes(Stream.of(split).map(s -> {
//                String[] ss = s.split(":");
//                return new RedisNode(ss[0], Integer.parseInt(ss[1]));
//            }).collect(Collectors.toList()));
//            redisClusterConfiguration.setPassword(RedisPassword.of(pwd));
//            if(isLettuce){
//                redisConnectionFactory = new LettuceConnectionFactory(redisClusterConfiguration);
//            }else {
//                redisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration);
//            }
//        }
//        return redisConnectionFactory;
//    }


}
