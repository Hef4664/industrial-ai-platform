package com.shengda.industrialaiplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat_message")
public class ChatMessage {
    @Id
    private String id;
    private String sessionId;
    private String role;
    private String content;
    private LocalDateTime createTime;


}
