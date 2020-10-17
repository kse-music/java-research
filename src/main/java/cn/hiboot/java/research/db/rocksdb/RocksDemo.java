package cn.hiboot.java.research.db.rocksdb;

import org.junit.jupiter.api.Test;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import java.util.Arrays;
import java.util.List;
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
    public void test() throws RocksDBException {
        RocksDBHelper.execute(dbPath, rocksDB -> {
            try{
                RocksDBHelper.put(rocksDB,"hello","world");
                byte[] value = RocksDBHelper.get(rocksDB,"hello");

//                rocksDB.put(ByteUtil.bytes("hello"), ByteUtil.bytes("world"));
//                byte[] value = rocksDB.get(ByteUtil.bytes("hello"));
                System.out.println(new String(value));
            }catch (RocksDBException e){
                e.printStackTrace();
            }
        });
    }


    //  RocksDB.DEFAULT_COLUMN_FAMILY
    @Test
    public void testDefaultColumnFamily() throws RocksDBException {
        RocksDBHelper.execute(dbPath,rocksDB -> {
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
