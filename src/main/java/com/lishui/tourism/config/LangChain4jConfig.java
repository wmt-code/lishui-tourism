package com.lishui.tourism.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lishui.tourism.service.ai.MockChatModel;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;

/**
 * LangChain4j AI 配置
 */
@Slf4j
@Configuration
public class LangChain4jConfig {
    
    @Value("${langchain4j.mock-mode:true}")
    private Boolean mockMode;
    
    @Value("${langchain4j.openai.api-key:}")
    private String apiKey;
    
    @Value("${langchain4j.openai.model-name:gpt-3.5-turbo}")
    private String modelName;
    
    @Value("${langchain4j.openai.base-url:https://api.openai.com/v1}")
    private String baseUrl;
    
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        // 如果是 Mock 模式或没有配置 API Key，使用 Mock 实现
        if (mockMode || apiKey == null || apiKey.isEmpty()) {
            log.warn("=== AI Mock 模式启动 ===");
            log.warn("当前使用 Mock AI 服务，无需真实 API Key 即可体验功能");
            log.warn("如需使用真实 AI，请配置 langchain4j.openai.api-key 并设置 mock-mode=false");
            return new MockChatModel();
        }
        
        // 使用真实的 OpenAI
        log.info("=== AI 真实模式启动 ===");
        log.info("使用 OpenAI 模型：{}", modelName);
        
        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .baseUrl(baseUrl)
                .temperature(0.7)
                .maxTokens(2000)
                .build();
    }
}
