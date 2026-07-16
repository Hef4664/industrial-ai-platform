package com.shengda.industrialaiplatform.service.impl;

import com.shengda.industrialaiplatform.dao.ChatMessageDao;
import com.shengda.industrialaiplatform.entity.ChatMessage;
import com.shengda.industrialaiplatform.service.ChatMessageService;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 问答接口实现类
 */
@Service
public class ChatMessageServiceImpl implements ChatMessageService {
     @Autowired
     private ChatMessageDao chatMessageDao;
     @Autowired
     private QwenChatModel qwenChatModel;
    /**
     * 大模型问答方法
      * @param sessionId
     * @param userMessage
     * @return
     */
    @Override
    public ChatMessage chat(String sessionId, String userMessage) {
        /**存储用户问题信息**/
        ChatMessage  userChatMessage = new ChatMessage();
        userChatMessage.setSessionId(sessionId);
        userChatMessage.setRole("user");
        userChatMessage.setContent(userMessage);
        userChatMessage.setCreateTime(LocalDateTime.now());
        chatMessageDao.save(userChatMessage);

        /**调用千问大模型,并接收返回消息**/
        String replyMessage =  qwenChatModel.chat(userMessage);

        /*AI回答的信息保存在mogodb中*/
        ChatMessage aiMessage = new ChatMessage();
        aiMessage.setSessionId(sessionId);
        aiMessage.setContent(replyMessage);
        aiMessage.setRole("AI");
        aiMessage.setCreateTime(LocalDateTime.now());
        return aiMessage;



    }
}
