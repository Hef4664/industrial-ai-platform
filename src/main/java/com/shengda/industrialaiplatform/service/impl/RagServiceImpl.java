package com.shengda.industrialaiplatform.service.impl;

import com.shengda.industrialaiplatform.dao.IndustrialDocumentDao;
import com.shengda.industrialaiplatform.entity.IndustrialDocument;
import com.shengda.industrialaiplatform.service.RagService;
import dev.langchain4j.community.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * rag内容检索功能的业务实现,该类中设计检索功能具体实现
 */
@Service
public class RagServiceImpl implements RagService {
    @Autowired
    //文档持久层对象
    private IndustrialDocumentDao industrialDocumentDao;
    @Autowired
    private QwenEmbeddingModel embeddingModel;
    //接收嵌入内存对象
    private InMemoryEmbeddingStore<TextSegment> embeddingStore;
    //接收检索器对象
    private EmbeddingStoreContentRetriever retriever;

    /**
     * 初始化知识库
     */
    @PostConstruct//使当前方法在本类创建后自动执行
    public  void initKnowledegeBase(){
        // 1. 初始化内存的嵌入存储
        embeddingStore =   new InMemoryEmbeddingStore <>();
        // 2. 文档分块器：把长文档切成小块，每块500个token，重叠50个token，避免切断语义
        var spliter = DocumentSplitters.recursive(500, 50);
        // 3. 摄取器：把文档分快、转成向量、存入到嵌入存储里
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(spliter)
                .embeddingModel(embeddingModel) // 嵌入模型
                .embeddingStore(embeddingStore) // 嵌入存储
                .build();
        // 4. 加载所有的工业文档
        List<IndustrialDocument>  list = industrialDocumentDao.findAll();


        for (IndustrialDocument industrialDocument : list) {
            // 把我们的工业文档转成LangChain4j的Document
            Document document = Document.from(industrialDocument.getContent());

            System.out.println(document+"**********************");
            // 摄取文档
            ingestor.ingest(document);

        }


        // 5. 创建内容检索器，最多返回3个最相关的文档片段
        retriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore) // 嵌入存储
                .embeddingModel(embeddingModel) // 嵌入模型
                .maxResults(3) // 最多返回3个最相关的文档片段
                .minScore(0.7) // 相似度最低0.7，低于这个的就不要了
                .build();

    }
    @Override
    public String retrieveRelevantContent(String query) {
        //判断问题是否为空白，若为空白，则直接返回""
        if (query == null || query.isEmpty()) {
            return "";
        }
        // 用检索器检索相关的片段
        List<Content> segments = retriever.retrieve(Query.from(query));
        //判断是否从内存库中检索到相关内容，如果没有，返回''
        if (segments.isEmpty()) {
            return "";
        }
        //检索到相关内容，就把内容拼接成一个字符串返回
        // 创建可变字符串，用于存储拼接内容
        StringBuilder context = new StringBuilder();
        //遍历检索结果集合，进行内容拼接
        for (Content segment : segments){
            context.append(segment.textSegment().text()).append("\n\n");
        }
        return context.toString();

    }
}
