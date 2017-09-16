# LuceneDemo
这是初学Lucene时的一个小Demo

==========================================================
<h5>Lucene的核心步骤是：先采集数据(创建索引)---〉再搜索索引。</h5>

<h4>一、采集索引：</h4>
    IndexWriter是索引过程的核心组件，通过IndexWriter可以创建新索引、更新索引、删除索引操作。IndexWriter需要通过Directory对索引进行存储操作。
    Directory描述了索引的存储位置，底层封装了I/O操作，负责对索引进行存储。它是一个抽象类，它的子类常用的包括FSDirectory（在文件系统存储索引）、
    RAMDirectory（在内存存储索引）。
    
    
    IndexWriter writer = new IndexWriter(FSDirectory.open(new File("G:\\test")),//索引库的位置
                                          new IndexWriterConfig(Version.LUCENE_4_10_3, //lucence版本，注意要和引用的jar包版本一致
                                                                 new StandardAnalyzer())//标准分词器，常用的中文分词器：new IKAnalyzer()
    Document doc = new Document();
    Field id = new StringField("id", book.getId().toString(), Store.YES);
    doc.add(id);
    writer.addDocument(doc);
<h4>二、搜索索引（常规）：</h4>
    同数据库的sql一样，lucene全文检索也有固定的语法：<br/>
    最基本的有比如：AND, OR, NOT 等<br/>
    举个例子，用户想找一个detail中包括java关键字和lucene关键字的文档。<br/>
    它对应的查询语句：detail:java AND lucene<br/>
    IndexSearcher是搜索过程的核心组件。
    
    //创建Query对象
    Query query = new QueryParser("createTime",new StandardAnalyzer()).parse("detail:java AND lucene");
    IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("G:\\test")));
    TopDocs topDocs = searcher.search(query, 10);//查询匹配度最高的10条记录
		int count = topDocs.totalHits;//实际匹配记录数
    ScoreDoc[] scoreDocs = topDocs.scoreDocs;//结果集封装在ScoreDoc[]中
    遍历scoreDocs，通过int docId = scoreDoc.doc获取文档对象id
    Document doc = searcher.doc(docId);//通过文档对象Id获取文档
