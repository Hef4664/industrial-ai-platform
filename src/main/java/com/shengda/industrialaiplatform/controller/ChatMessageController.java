package com.shengda.industrialaiplatform.controller;

import com.shengda.industrialaiplatform.entity.ChatMessage;
import com.shengda.industrialaiplatform.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api") //http://localhost:8080/api
public class ChatMessageController {
    @Autowired
    @Qualifier("chatService2")
    private ChatMessageService chatMessageService;
    @PostMapping("chat") //http://localhost:8080/api/chat
    public ChatMessage aiChat(@RequestBody Map<String, Object> map){
        String sesionId =  (String) map.get("sessionId");
        String userMessage = (String) map.get("userMessage");

        ChatMessage replyMessage = chatMessageService.chat(sesionId,userMessage);
        return replyMessage;



    }
    @GetMapping("history/{sessionId}")//http://localhost:8080/api/history
    public List<ChatMessage> showHistory(@PathVariable("sessionId") String sessionId){
        List <ChatMessage> history = chatMessageService.getHistory(sessionId);
        return history;

    }
    @PostMapping("ragChat")
    public  String ragChat(@RequestBody Map<String, Object> map){
        String sessionId = (String)map.get("sessionId");
        String userMessage = (String)map.get("userMessage");
        String deviceType = (String)map.get("deviceType");
        String replyMessage = chatMessageService.ragChat(sessionId,userMessage,deviceType);
        return replyMessage;

    }
}
