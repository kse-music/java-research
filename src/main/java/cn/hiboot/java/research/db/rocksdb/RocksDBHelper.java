package cn.hiboot.java.research.db.rocksdb;

import org.rocksdb.*;
import org.rocksdb.util.ByteUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * description about this class
 *
 * @author DingHao
 * @since 2020/10/17 21:41
 */
public class RocksDBHelper {

    public static void execute(String dbPath, BiConsumer<RocksDB,Options> consumer) throws RocksDBException {
        try (final Options options = new Options().setCreateIfMissing(true)) {
            mkdir(dbPath);
            try (final RocksDB rocksDB = RocksDB.open(options, dbPath)) {
                consumer.accept(rocksDB,options);
            }
        }
    }

    private static boolean mkdir(String path){
        File file = new File(path);
        if(file.exists()){
            return false;
        }
        return file.mkdirs();
    }

    public static void destroyDB(String dbPath) throws RocksDBException {
        RocksDBHelper.execute(dbPath, (rocksDB,options) -> {
            try{
                rocksDB.close();
                RocksDB.destroyDB(dbPath,options);
            }catch (RocksDBException e){
                e.printStackTrace();
            }
        });
    }

    public static void execute(String dbPath, String cfName,BiConsumer<RocksDB,List<ColumnFamilyHandle>> consumer) throws RocksDBException {
        try (final ColumnFamilyOptions cfOpts = new ColumnFamilyOptions().optimizeUniversalStyleCompaction()) {
            try (final DBOptions options = new DBOptions().setCreateIfMissing(true).setCreateMissingColumnFamilies(true)) {
                mkdir(dbPath);
                // list of column family descriptors, first entry must always be default column family
                final List<ColumnFamilyDescriptor> cfDescriptors = Arrays.asList(
                        new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, cfOpts),
                        new ColumnFamilyDescriptor(cfName.getBytes(), cfOpts)
                );
                List<ColumnFamilyHandle> cfHandles = new ArrayList<>();
                try (final RocksDB rocksDB = RocksDB.open(options, dbPath, cfDescriptors, cfHandles)) {
                    consumer.accept(rocksDB,cfHandles);
                }finally {
                    // NOTE frees the column family handles before freeing the db
                    for (final ColumnFamilyHandle cfHandle : cfHandles) {
                        cfHandle.close();
                    }
                }
            }
        }
    }

    public static void put(RocksDB rocksDB,String key,Object value) throws RocksDBException {
        rocksDB.put(ByteUtil.bytes(key), ByteUtil.bytes(value.toString()));
    }

    public static byte[] get(RocksDB rocksDB,String key) throws RocksDBException {
       return rocksDB.get(ByteUtil.bytes(key));
    }

}
