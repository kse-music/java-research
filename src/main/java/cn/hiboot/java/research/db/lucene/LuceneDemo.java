package cn.hiboot.java.research.db.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * describe about this class
 *
 * @author DingHao
 * @since 2020/1/21 16:27
 */
public class LuceneDemo {

    private static final String DATA_PATH = "g:/data/indexDir";

    private Directory directory = FSDirectory.open(Paths.get(DATA_PATH));
    private Analyzer analyzer = new IKAnalyzer();


    public LuceneDemo() throws IOException {

    }

    @Test
    public void create() throws Exception{
        //1 创建文档对象
        Document document = new Document();
        // 创建并添加字段信息。参数：字段的名称、字段的值、是否存储，这里选Store.YES代表存储到文档列表。Store.NO代表不存储
        document.add(new StringField("id", "1", Field.Store.YES));
        // 这里我们title字段需要用TextField，即创建索引又会被分词。StringField会创建索引，但是不会被分词
        document.add(new TextField("title", "谷歌地图之父跳槽facebook", Field.Store.YES));
        document.add(new StringField("content", "这是一篇关于谷歌的新闻内容", Field.Store.NO));
        document.add(new StringField("publish", "2020-01-21", Field.Store.YES));
        document.add(new StoredField("age", 31));
        document.add(new StoredField("weight", 75d));
        document.add(new StoredField("height", 1.73f));

        //2 索引目录类,指定索引在硬盘中的位置

        //3 创建分词器对象

        //4 索引写出工具的配置对象
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        //5 创建索引的写出工具类。参数：索引的目录和配置信息
        IndexWriter indexWriter = new IndexWriter(directory, conf);

        //6 把文档交给IndexWriter
        indexWriter.addDocument(document);
        //7 提交
        indexWriter.commit();
        //8 关闭
        indexWriter.close();
    }

    @Test
    public void search() throws Exception {
        // 索引目录对象

        // 索引读取工具
        IndexReader reader = DirectoryReader.open(directory);
        // 索引搜索工具
        IndexSearcher searcher = new IndexSearcher(reader);

        // 创建查询解析器,两个参数：默认要查询的字段的名称，分词器
        QueryParser parser = new QueryParser("title", analyzer);
        // 创建查询对象
        Query query = parser.parse("谷歌");

        // 搜索数据,两个参数：查询条件对象要查询的最大结果条数
        // 返回的结果是 按照匹配度排名得分前N名的文档信息（包含查询到的总条数信息、所有符合条件的文档的编号信息）。
        TopDocs topDocs = searcher.search(query, 10);
        // 获取总条数
        System.out.println("本次搜索共找到" + topDocs.totalHits + "条数据");
        // 获取得分文档对象（ScoreDoc）数组.ScoreDoc中包含：文档的编号、文档的得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 取出文档编号
            int docID = scoreDoc.doc;
            // 根据编号去找文档
            Document doc = reader.document(docID);
            System.out.println("id: " + doc.get("id"));
            System.out.println("title: " + doc.get("title"));
            System.out.println("content: " + doc.get("content"));
            System.out.println("publish: " + doc.get("publish"));
            System.out.println("age: " + doc.get("age"));
            System.out.println("weight: " + doc.get("weight"));
            System.out.println("height: " + doc.get("height"));
            // 取出文档得分
            System.out.println("得分： " + scoreDoc.score);
        }
    }

    @Test
    public void update() throws Exception{
        // 创建目录对象

        // 创建配置对象
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        // 创建索引写出工具
        IndexWriter writer = new IndexWriter(directory, conf);

        // 创建新的文档数据
        Document doc = new Document();
        doc.add(new StringField("id","2", Field.Store.YES));
        doc.add(new TextField("title","谷歌地图之父跳槽facebook ", Field.Store.YES));
        /* 修改索引。参数：
         * 	词条：根据这个词条匹配到的所有文档都会被修改
         * 	文档信息：要修改的新的文档数据
         */
        writer.updateDocument(new Term("id","1"), doc);
        // 提交
        writer.commit();
        // 关闭
        writer.close();
    }

    @Test
    public void delete() throws Exception {
        // 创建目录对象

        // 创建配置对象
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        // 创建索引写出工具
        IndexWriter writer = new IndexWriter(directory, conf);

        // 根据词条进行删除
        writer.deleteDocuments(new Term("id", "1"));

        // 根据query对象删除,如果ID是数值类型，那么我们可以用数值范围查询锁定一个具体的ID
//        Query query = NumericDocValuesField.newSlowRangeQuery("id", 0L, 3L);
//        writer.deleteDocuments(query);

        // 删除所有
//        writer.deleteAll();
        // 提交
        writer.commit();
        // 关闭
        writer.close();
    }

}
