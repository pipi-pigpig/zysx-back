package com.nurturing.config;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nurturing.entity.ChatSentence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;



@Configuration
public class CaffeineConfig {

    @Value("${chat.history.memory.caffeine.maximum-size:200}")
    private int HistoryMaximumSize;

    @Bean("historyCache") // 指定 Bean 名称，便于注入时区分
    public Cache<String, List<ChatSentence>> historyCache() {
        return Caffeine.newBuilder()
                .maximumSize(HistoryMaximumSize) // 使用 history 特定的配置
                .build();
    }
}
