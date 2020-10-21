package cn.hiboot.java.research.db.rocksdb;

import org.junit.jupiter.api.Test;
import org.rocksdb.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2020/3/24 23:04
 */
public class RocksDemo {

    private static final String BASE_PATH = "/work/data/rocksdb/";
    private static final String dbPath = BASE_PATH.concat("dh_test");

    @Test
    public void test() throws RocksDBException{
        RocksDBHelper.execute(dbPath, (rocksDB,options) -> {
            try{
                byte[] value = RocksDBHelper.get(rocksDB,"甲Ⅱ电容器电缆B相");
//                byte[] value = rocksDB.get(ByteUtil.bytes("hello"));
                System.out.println(new String(value));
            }catch (RocksDBException e){
                e.printStackTrace();
            }
        });
    }

    @Test
    public void put() throws RocksDBException, IOException {
        Map<Long,String> data = data(10000000);
        RocksDBHelper.execute(dbPath, (rocksDB,options) -> {
            try{
                long s = System.currentTimeMillis();
                for (Map.Entry<Long, String> entry : data.entrySet()) {
                    RocksDBHelper.put(rocksDB,entry.getValue(),entry.getKey());
                }
                System.out.println(System.currentTimeMillis()-s);
            }catch (RocksDBException e){
                e.printStackTrace();
            }
        });
    }

    @Test
    public void batch() throws RocksDBException, IOException {
        Map<Long,String> data = data(10000000);
        Map<String,Integer> map = new LinkedHashMap<>();
        map.put("dh_test"+1,1000000);
        map.put("dh_test"+2,100000);
        map.put("dh_test"+3,10000);
        map.put("dh_test"+4,1000);
        map.put("dh_test"+5,100);
        map.forEach((k,v) -> {
            try {
                RocksDBHelper.executeBatch(BASE_PATH.concat(k), (rocksDB,options) -> {
                    try{
                        long s = System.currentTimeMillis();
                        int i = 0;
                        WriteOptions writeOpt = new WriteOptions();
                        WriteBatch batch = new WriteBatch();
                        for (Map.Entry<Long, String> entry : data.entrySet()) {
                            RocksDBHelper.put(batch,entry.getValue(),entry.getKey());
                            i++;
                            if(i % v == 0){
                                rocksDB.write(writeOpt, batch);
                                batch.close();
                                batch = new WriteBatch();
                                i = 0;
                            }
                        }
                        if(i != 0){
                            rocksDB.write(writeOpt, batch);
                            writeOpt.close();
                            batch.close();
                        }
                        System.out.println("batch size " + v +" cost "+(System.currentTimeMillis()-s));
                    }catch (RocksDBException e){
                        e.printStackTrace();
                    }
                });
            } catch (RocksDBException e) {
                e.printStackTrace();
            }
        });
    }

    private Map<Long,String> data(int size) throws IOException {
        Map<Long,String> data = new HashMap<>(size);
        List<String> strings = Files.readAllLines(Paths.get("/work/data.txt"));
        for (String str : strings) {
            String[] split = str.split("===");
            data.put(Long.parseLong(split[0]),split[1]);
        }
        return data;
    }

    @Test
    public void destroy() throws RocksDBException {
        RocksDBHelper.destroyDB(dbPath);
    }


    //  RocksDB.DEFAULT_COLUMN_FAMILY
    @Test
    public void testDefaultColumnFamily() throws RocksDBException {
        RocksDBHelper.execute(dbPath,(rocksDB,options) -> {
            try{
                // 简单key-value
                byte[] key = "Hello".getBytes();
                rocksDB.put(key, "World".getBytes());

                System.out.println(new String(rocksDB.get(key)));

                rocksDB.put("SecondKey".getBytes(), "SecondValue".getBytes());

                // 通过List做主键查询
                List<byte[]> keys = Arrays.asList(key, "SecondKey".getBytes(), "missKey".getBytes());
                List<byte[]> values = rocksDB.multiGetAsList(keys);
                for (int i = 0; i < keys.size(); i++) {
                    System.out.println("multiGet " + new String(keys.get(i)) + ":" + (values.get(i) != null ? new String(values.get(i)) : null));
                }

                // 打印全部[key - value]
                RocksIterator iter = rocksDB.newIterator();
                for (iter.seekToFirst(); iter.isValid(); iter.next()) {
                    System.out.println("iterator key:" + new String(iter.key()) + ", iter value:" + new String(iter.value()));
                }

                // 删除一个key
                rocksDB.delete(key);
                System.out.println("after remove key:" + new String(key));

                iter = rocksDB.newIterator();
                for (iter.seekToFirst(); iter.isValid(); iter.next()) {
                    System.out.println("iterator key:" + new String(iter.key()) + ", iter value:" + new String(iter.value()));
                }
            }catch (RocksDBException e){
                e.printStackTrace();
            }
        });
    }

    // 使用特定的列族打开数据库，可以把列族理解为关系型数据库中的表(table)
    @Test
    public void testCertainColumnFamily() throws RocksDBException {
        String cfName = "my-first-columnfamily";
        RocksDBHelper.execute(dbPath,cfName,(rocksDB,cfHandles) -> {
            try {
                ColumnFamilyHandle cfHandle = cfHandles.stream().filter(x -> {
                    try {
                        return (new String(x.getName())).equals(cfName);
                    } catch (RocksDBException e) {
                        return false;
                    }
                }).collect(Collectors.toList()).get(0);

                // 写入key/value
                String key = "FirstKey";
                rocksDB.put(cfHandle, key.getBytes(), "FirstValue".getBytes());
                // 查询单key
                byte[] getValue = rocksDB.get(cfHandle, key.getBytes());
                System.out.println("get Value : " + new String(getValue));
                // 写入第2个key/value
                rocksDB.put(cfHandle, "SecondKey".getBytes(), "SecondValue".getBytes());

                List<byte[]> keys = Arrays.asList(key.getBytes(), "SecondKey".getBytes());
                List<ColumnFamilyHandle> cfHandleList = Arrays.asList(cfHandle, cfHandle);
                // 查询多个key
                List<byte[]> values = rocksDB.multiGetAsList(cfHandleList, keys);
                for (int i = 0; i < keys.size(); i++) {
                    System.out.println("multiGet:" + new String(keys.get(i)) + "--" + (values.get(i) == null ? null : new String(values.get(i))));
                }

                // 删除单key
                rocksDB.delete(cfHandle, key.getBytes());

                RocksIterator iter = rocksDB.newIterator(cfHandle);
                for (iter.seekToFirst(); iter.isValid(); iter.next()) {
                    System.out.println("iterator:" + new String(iter.key()) + ":" + new String(iter.value()));
                }
            } catch (RocksDBException e) {
                e.printStackTrace();
            }
        });

    }


}
