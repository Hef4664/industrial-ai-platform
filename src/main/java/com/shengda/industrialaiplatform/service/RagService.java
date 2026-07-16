package com.shengda.industrialaiplatform.service;

/**
 * rag业务接口
 */
public interface RagService {
    //内容检索业务
    public String retrieveRelevantContent(String query);
}
