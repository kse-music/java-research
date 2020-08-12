package cn.hiboot.java.research.algorithm;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 最近最少使用
 *
 * @author DingHao
 * @since 2019/4/2 10:26
 */
public class LRULinkedHashMap<K,V> extends LinkedHashMap<K,V>{

    private int size;

    public LRULinkedHashMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public LRULinkedHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor,true);
        this.size = initialCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size < size();
    }

    @Test
    public void lru(){
        LRULinkedHashMap<String,String> lru = new LRULinkedHashMap<>(4);
        lru.put("1","one");
        lru.put("2","two");
        lru.put("3","three");
        lru.put("4","four");

        lru.get("1");
        lru.put("5","five");

        lru.forEach((k,v) -> System.out.println(k+" = "+v));
    }

}
