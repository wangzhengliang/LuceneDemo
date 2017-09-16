package com.study.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import com.study.dao.ItemsDao;
import com.study.dao.impl.ItemsDaoImpl;
import com.study.domain.Items;

/**
 * LuceneDemo 核心
 * @author zhengliang
 *
 */
public class LuceneDemoTest {

	/**
	 * 创建索引
	 * @throws IOException 
	 */
	@Test
	public void testCreateIndex() throws IOException{
		ItemsDao itemsDao = new ItemsDaoImpl();
		List<Items> items = itemsDao.queryItems();
		
		List<Document> documents = new ArrayList<Document>();
		Document document;
		for (Items item : items) {
			document = new Document();
			//Sotre:如果是yes，则说明存储到文档域中
			Field id = new TextField("id",item.getId().toString(),Store.YES);
			Field name = new TextField("name",item.getName(),Store.YES);
			Field price = new TextField("price",item.getPrice()+"",Store.YES);
			Field detail = new TextField("detail",item.getDetail(),Store.YES);
			Field createTime = new TextField("createTime",item.getCreateTime().toString(),Store.YES);
			document.add(id);
			document.add(name);
			document.add(price);
			document.add(detail);
			document.add(createTime);
			
			documents.add(document);
		}
		
		//创建分词器   标准分词器
		Analyzer analyzer = new StandardAnalyzer();
		
		//创建IndexWriter
	    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
	    File indexFile = new File("G:\\luceneDemoIndexFile");//索引库位置
	    Directory directory = FSDirectory.open(indexFile);//索引流对象
	    IndexWriter writer = new IndexWriter(directory,indexWriterConfig);
	    for (Document doc : documents) {
			writer.addDocument(doc);
		}
	    writer.close();
	}
	
	/**
	 * 索搜索引
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@Test
	public void testIndexSearcher() throws ParseException, IOException{
		//创建query对象
		//第一个参数：搜索域的名称；第二个参数：和创建索引时使用同一个分词器
		QueryParser queryParser = new QueryParser("createTime",new StandardAnalyzer());
		Query query = queryParser.parse("createTime:2015 AND 02");//通过QueryParser创建Query对象
																  //如果有多个条件，关键词记得都要大写
		//创建IndexSearcher
		//指定索引库的位置
		File indexFile = new File("G:\\luceneDemoIndexFile");
		Directory directory = FSDirectory.open(indexFile);
		DirectoryReader reader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		
		//第二个参数：指定返回最佳匹配的十条记录
		TopDocs topDocs = indexSearcher.search(query, 10);
		
		//实际匹配记录数
		int count = topDocs.totalHits;
		System.out.println("匹配记录数"+count);
		
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			System.out.println("==========================");
			//获取文档Id
			int docId = scoreDoc.doc;
			
			//通过Id获取文档
			Document doc = indexSearcher.doc(docId);
			System.out.println("商品Id:"+doc.get("id"));
			System.out.println("商品name:"+doc.get("name"));
			System.out.println("商品price:"+doc.get("price"));
			System.out.println("商品detail:"+doc.get("detail"));
			System.out.println("商品createTime:"+doc.get("createTime"));
		}
	}
	
	
	/**
	 * 对testCreateIndex做了一些修改，使之有更加精确的Filed定义
	 * @throws Exception
	 */
	@Test
	public void testCreateIndexAfter() throws Exception{
		ItemsDao itemsDao = new ItemsDaoImpl();
		List<Items> items = itemsDao.queryItems();
		
		List<Document> documents = new ArrayList<Document>();
		Document document;
		for (Items item : items) {
			document = new Document();
			//Sotre:如果是yes，则说明存储到文档域中
			Field id = new StringField("id",item.getId().toString(),Store.YES);
			Field name = new TextField("name",item.getName(),Store.YES);
			Field price = new DoubleField("price",item.getPrice(),Store.YES);
			Field detail = new TextField("detail",item.getDetail(),Store.YES);
			Field createTime = new TextField("createTime",item.getCreateTime().toString(),Store.YES);
			document.add(id);
			document.add(name);
			document.add(price);
			document.add(detail);
			document.add(createTime);
			
			documents.add(document);
		}
		
		//创建分词器   标准分词器
		Analyzer analyzer = new StandardAnalyzer();
		
		//创建IndexWriter
	    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
	    File indexFile = new File("G:\\luceneDemoIndexFile");//索引库位置
	    Directory directory = FSDirectory.open(indexFile);//索引流对象
	    IndexWriter writer = new IndexWriter(directory,indexWriterConfig);
	    for (Document doc : documents) {
			writer.addDocument(doc);
		}
	    writer.close();
	}
	
	
	/**
	 * 删除索引
	 * 	    不管是创建索引还是删除索引，都是通过IndexWriter完成的
	 * @throws IOException
	 */
	@Test
	public void testDeleteIndex() throws IOException{
		
		Analyzer analyzer = new StandardAnalyzer();
		
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
		
		Directory directory = FSDirectory.open(new File("G:\\lucnenDemoIndexFile"));
		
		IndexWriter writer = new IndexWriter(directory, indexWriterConfig);
		
		//writer.deleteDocuments(new Term("id","1"));
		
		writer.deleteAll();
		
		
	}
}
