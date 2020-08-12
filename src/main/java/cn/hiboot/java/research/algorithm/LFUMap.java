package cn.hiboot.java.research.algorithm;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 最不经常使用
 *
 * @author DingHao
 * @since 2019/4/2 10:26
 */
public class LFUMap<K,V>{

    private int size;

    private Map<K,LFU<K,V>> map;

    public LFUMap(int initialCapacity) {
        this.map = new HashMap<>(initialCapacity);
        this.size = initialCapacity;
    }

    public V put(K key, V value) {
        LFU<K, V> kvlfu = map.get(key);
        if(kvlfu != null){
            return value;
        }
        map.put(key,new LFU<>(key,value,1,System.nanoTime()));
        if(map.size() > size){
            remove();
        }
        return null;
    }

    public V get(K key){
        LFU<K, V> kvlfu = map.get(key);
        if(kvlfu != null){
            kvlfu.count++;
            kvlfu.lastTime = System.nanoTime();
            return kvlfu.v;
        }
        return null;
    }

    public V remove(K key){
        LFU<K, V> remove = map.remove(key);
        if(remove != null){
            return remove.v;
        }
        return null;
    }

    private void remove(){
        LFU<K, V> min = Collections.min(map.values());
        map.remove(min.k);
    }

    @Override
    public String toString() {
        map.values().stream().forEach(System.out::println);
        return "";
    }

    static class LFU<K,V> implements Comparable<LFU<K,V>>{

        K k;
        V v;
        Integer count;
        Long lastTime;

        public LFU(K k, V v, Integer count, Long lastTime) {
            this.k = k;
            this.v = v;
            this.count = count;
            this.lastTime = lastTime;
        }

        @Override
        public int compareTo(LFU<K,V> o) {
            int c = Integer.compare(count,o.count);
            return c == 0 ? Long.compare(lastTime,o.lastTime) : c;
        }

        @Override
        public String toString() {
            return "LFU{" +
                    "k=" + k +
                    ", v=" + v +
                    ", count=" + count +
                    ", lastTime=" + lastTime +
                    '}';
        }

    }

    @Test
    public void lfu(){
        LFUMap<String,String> lfu = new LFUMap<>(4);
        lfu.put("1","one");
        lfu.put("2","two");
        lfu.put("3","three");
        lfu.put("4","four");

        lfu.get("1");
        lfu.put("5","five");
        lfu.get("3");
        lfu.put("6","six");

        System.out.println(lfu);

    }

}
