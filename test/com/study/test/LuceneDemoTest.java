package com.study.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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
	
}
