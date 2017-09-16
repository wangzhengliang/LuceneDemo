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
 * LuceneDemo ����
 * @author zhengliang
 *
 */
public class LuceneDemoTest {

	/**
	 * ��������
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
			//Sotre:�����yes����˵���洢���ĵ�����
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
		
		//�����ִ���   ��׼�ִ���
		Analyzer analyzer = new StandardAnalyzer();
		
		//����IndexWriter
	    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
	    File indexFile = new File("G:\\luceneDemoIndexFile");//������λ��
	    Directory directory = FSDirectory.open(indexFile);//����������
	    IndexWriter writer = new IndexWriter(directory,indexWriterConfig);
	    for (Document doc : documents) {
			writer.addDocument(doc);
		}
	    writer.close();
	}
	
	/**
	 * ��������
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@Test
	public void testIndexSearcher() throws ParseException, IOException{
		//����query����
		//��һ������������������ƣ��ڶ����������ʹ�������ʱʹ��ͬһ���ִ���
		QueryParser queryParser = new QueryParser("createTime",new StandardAnalyzer());
		Query query = queryParser.parse("createTime:2015 AND 02");//ͨ��QueryParser����Query����
																  //����ж���������ؼ��ʼǵö�Ҫ��д
		//����IndexSearcher
		//ָ���������λ��
		File indexFile = new File("G:\\luceneDemoIndexFile");
		Directory directory = FSDirectory.open(indexFile);
		DirectoryReader reader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		
		//�ڶ���������ָ���������ƥ���ʮ����¼
		TopDocs topDocs = indexSearcher.search(query, 10);
		
		//ʵ��ƥ���¼��
		int count = topDocs.totalHits;
		System.out.println("ƥ���¼��"+count);
		
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			System.out.println("==========================");
			//��ȡ�ĵ�Id
			int docId = scoreDoc.doc;
			
			//ͨ��Id��ȡ�ĵ�
			Document doc = indexSearcher.doc(docId);
			System.out.println("��ƷId:"+doc.get("id"));
			System.out.println("��Ʒname:"+doc.get("name"));
			System.out.println("��Ʒprice:"+doc.get("price"));
			System.out.println("��Ʒdetail:"+doc.get("detail"));
			System.out.println("��ƷcreateTime:"+doc.get("createTime"));
		}
	}
	
	
	/**
	 * ��testCreateIndex����һЩ�޸ģ�ʹ֮�и��Ӿ�ȷ��Filed����
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
			//Sotre:�����yes����˵���洢���ĵ�����
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
		
		//�����ִ���   ��׼�ִ���
		Analyzer analyzer = new StandardAnalyzer();
		
		//����IndexWriter
	    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
	    File indexFile = new File("G:\\luceneDemoIndexFile");//������λ��
	    Directory directory = FSDirectory.open(indexFile);//����������
	    IndexWriter writer = new IndexWriter(directory,indexWriterConfig);
	    for (Document doc : documents) {
			writer.addDocument(doc);
		}
	    writer.close();
	}
	
	
	/**
	 * ɾ������
	 * 	    �����Ǵ�����������ɾ������������ͨ��IndexWriter��ɵ�
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
