package com.shengda.industrialaiplatform.service;

import com.shengda.industrialaiplatform.entity.ChatMessage;
import dev.langchain4j.service.UserMessage;

import java.util.List;

/**
 * 设计问答消息处理的业务方法
 */
public interface ChatMessageService {
    //问答方法
    public ChatMessage chat(String sessionId, String userMessage );

    //根据id查询会话历史，新版本jdk允许在接口中定义默认方法，该方法可以设置方法体
    public default List<ChatMessage> getHistory(String sessionId){

        return null;
    }
    //rag问答对话
    public  default String ragChat(String sessionId,String userMessage,String deviceType){
        return null;

    }


}
