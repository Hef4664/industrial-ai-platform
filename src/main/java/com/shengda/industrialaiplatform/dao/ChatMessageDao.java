package com.shengda.industrialaiplatform.dao;

import com.shengda.industrialaiplatform.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface  ChatMessageDao extends MongoRepository <ChatMessage,String>{
    //根据sessionId查询会话中所有信息的方法，将信息按时间降序排序
    public List<ChatMessage> findChatMessageBySessionIdOrderByCreateTimeDesc(String sessionId);
    //根据sessionId查询会话信息，并将信息按时间升序排序
    public List<ChatMessage> findChatMessageBySessionIdOrderByCreateTimeAsc(String sessionId);

}
