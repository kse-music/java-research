package cn.hiboot.java.research.db.rocksdb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2020/3/24 23:04
 */
public class RocksDemo {
    String dbPath = "/work/data/graph500";
    RocksDB db;

    @BeforeEach
    public void bf() throws RocksDBException {
        Options options = new Options();
        options.setCreateIfMissing(true);
        db = RocksDB.open(options,dbPath);
    }

    @Test
    public void test() throws RocksDBException {
        db.put("hello".getBytes(), "world".getBytes());
        byte[] value = db.get("hello".getBytes());
        System.out.println(new String(value));
    }
}
