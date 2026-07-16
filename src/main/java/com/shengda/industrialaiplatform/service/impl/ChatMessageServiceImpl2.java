package com.shengda.industrialaiplatform.service.impl;

import com.shengda.industrialaiplatform.dao.ChatMessageDao;
import com.shengda.industrialaiplatform.entity.ChatMessage;
import com.shengda.industrialaiplatform.service.ChatMessageService;
import com.shengda.industrialaiplatform.service.RagService;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * chatservice功能升级
 */
@Service("chatService2")
public class ChatMessageServiceImpl2 implements ChatMessageService {
     @Autowired
     private ChatMessageDao chatMessageDao;
     @Autowired
     @Qualifier("qwen2")
     private QwenChatModel qwenChatModel;

    /**
     * 调用大模型进行问答（升级为10轮记忆功能）
     * @param sessionId
     * @param userMessage
     * @return
     */
    @Override
    public ChatMessage chat(String sessionId, String userMessage) {
        /*补充用户信息详细内容，并保存到数据库*/
        ChatMessage  userChatMessage = new ChatMessage();
        userChatMessage.setSessionId(sessionId);
        userChatMessage.setRole("user");
        userChatMessage.setContent(userMessage);
        userChatMessage.setCreateTime(LocalDateTime.now());

        /*调用大模型回答，携带记忆*/
        List<ChatMessage> historyList = chatMessageDao.findChatMessageBySessionIdOrderByCreateTimeDesc(sessionId);
        /*创建拼接字符串，用于拼接历史信息*/
        StringBuilder question = new StringBuilder();
        question.append("历史信息如下：\n");
        /*遍历集合取信息*/
        if(historyList.size()>20){
            historyList = historyList.subList(0,20);

        }
        for (ChatMessage chatMessage : historyList) {
            if("user".equals(chatMessage.getRole())){
                question.append("用户："+chatMessage.getContent()+"\n");

            }
            else{
                question.append("AI回答:"+chatMessage.getContent()+"\n");
            }

        }
        /*拼接新问题*/
        question.append(userMessage+"\n");
        //调用千问大模型进行提问
        String replyMessage = qwenChatModel.chat(question.toString());

        /*补充大模型回答信息内容并存储到数据库*/
        ChatMessage aiMessage = new ChatMessage();
        aiMessage.setSessionId(sessionId);
        aiMessage.setContent(replyMessage);
        aiMessage.setCreateTime(LocalDateTime.now());
        aiMessage.setRole("AI");

        chatMessageDao.save(userChatMessage);
        chatMessageDao.save(aiMessage);

        return aiMessage;
    }
    /**
     * 获取会话历史
     */
    @Override
    public List<ChatMessage> getHistory(String sessionId) {
        //根据sessionid查询会话的数据库操作
        List<ChatMessage> historyList = chatMessageDao.findChatMessageBySessionIdOrderByCreateTimeAsc(sessionId);
        return historyList;
    }
    @Autowired
    private RagService ragService;
    //rag知识库检索问答

    /**
     *
     * @param sessionId
     * @param userMessage
     * @param deviceType
     * @return
     */
    @Override
    public String ragChat(String sessionId, String userMessage, String deviceType) {
        /**不补全用户问题信息存储到数据库*/
        ChatMessage userMsg = new ChatMessage();
        userMsg.setSessionId(sessionId);
        userMsg.setRole("user");
        userMsg.setContent(userMessage);
        userMsg.setCreateTime(LocalDateTime.now());
        chatMessageDao.save(userMsg);
        /**进行内容检索，调用大模型**/
        String content = ragService.retrieveRelevantContent(userMessage);
        //构建大模型提示词
        // 2. 构建prompt，告诉大模型只能根据上下文回答，它是工业运维专家
        String prompt = """
        你是一个资深的工业运维专家，你需要根据下面的工业设备文档来回答运维人员的问题。
        如果文档里没有相关的内容，你就说："抱歉，这个问题我暂时无法回答，请联系技术支持。"
        不要编造内容，不要回答文档以外的问题，回答要专业、简洁，符合运维的规范。
        当前处理的设备类型是：{{deviceType}}

        工业设备文档：
        {{context}}

        运维人员的问题：
        {{question}}
        """.replace("{{context}}", content).replace("{{question}}", userMessage).replace("{{deviceType}}", deviceType);
        //调用大模型回答
        String replyMessage = qwenChatModel.chat(prompt);
        /**补充详细内容存储到数据库**/
        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setSessionId(sessionId);
        aiMsg.setRole("ai");
        aiMsg.setContent(replyMessage);
        aiMsg.setCreateTime(LocalDateTime.now());
        chatMessageDao.save(aiMsg);
        return replyMessage;
    }
}
