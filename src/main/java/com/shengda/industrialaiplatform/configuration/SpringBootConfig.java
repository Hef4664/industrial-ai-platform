package com.shengda.industrialaiplatform.configuration;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 该类作为配置类，在该类中创建的对象，都由spring框架统一管理调用
 */
@Configuration
public class SpringBootConfig {
    @Value("${langchain4j.community.dashscope.chat-model.api-key}")
    private String qwenApiKey;

    @Bean("qwen2")
    public QwenChatModel qwenWebChatModel() {
        return QwenChatModel.builder()
                .apiKey(qwenApiKey)
                .modelName("qwen3.7-max")
                .temperature(0.7f)
                .maxTokens(2048)
                .enableSearch(true)
                .build();
    }

}
