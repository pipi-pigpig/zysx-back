package com.nurturing.chat.store.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nurturing.chat.store.ChatHistoryStore;
import com.nurturing.entity.ChatSentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;


@Component
@Primary // 优先级较低，当有 Redis 实现时，Redis 实现会被注入
public class CaffeineChatHistoryStore implements ChatHistoryStore {

    // 使用 Caffeine Cache
    private final Cache<String, List<ChatSentence>> historyCache;

    @Autowired
    public CaffeineChatHistoryStore(@Qualifier("historyCache") Cache<String, List<ChatSentence>> historyCache) {
        this.historyCache = historyCache; // 在构造函数中初始化 final 字段
    }

    @Transactional
    @Override
    public void saveQuestionHistory(String sessionId, String question) {

        ConcurrentMap<String, List<ChatSentence>> cacheAsMap = historyCache.asMap();

        ChatSentence questionSentence = new ChatSentence("user",question);

        // 如果 Key 不存在，computeIfPresent 不会执行。我们需要处理 Key 不存在的情况。
        // 使用 compute，它无论 Key 是否存在都会执行
        cacheAsMap.compute(sessionId, (key, existingList) -> {
            if (existingList == null) {
                // Key 不存在，创建一个新列表并添加消息
                List<ChatSentence> newList = new ArrayList<ChatSentence>();
                newList.add(questionSentence);
                return newList;
            } else {
                // Key 存在，确保列表是可变的并添加消息
                if (!(existingList instanceof ArrayList)) {
                    // 如果不是 ArrayList，创建一个新的 ArrayList 并复制内容
                    existingList = new ArrayList<ChatSentence>(existingList);
                }
                existingList.add(questionSentence);
                return existingList;
            }
        });
    }

    @Transactional
    @Override
    public void saveAnswerHistory(String sessionId, String answer) {

        ConcurrentMap<String, List<ChatSentence>> cacheAsMap = historyCache.asMap();

        ChatSentence answerSentence = new ChatSentence("assistant",answer);

        // 如果 Key 不存在，computeIfPresent 不会执行。我们需要处理 Key 不存在的情况。
        // 使用 compute，它无论 Key 是否存在都会执行
        cacheAsMap.compute(sessionId, (key, existingList) -> {
            if (existingList == null) {
                // Key 不存在，创建一个新列表并添加消息
                List<ChatSentence> newList = new ArrayList<ChatSentence>();
                newList.add(answerSentence);
                return newList;
            } else {
                // Key 存在，确保列表是可变的并添加消息
                if (!(existingList instanceof ArrayList)) {
                    // 如果不是 ArrayList，创建一个新的 ArrayList 并复制内容
                    existingList = new ArrayList<ChatSentence>(existingList);
                }
                existingList.add(answerSentence);
                return existingList;
            }
        });
    }

    @Override
    public List<ChatSentence> getHistory(String sessionId) {

        if(historyCache.getIfPresent(sessionId) == null){
            return new ArrayList<ChatSentence>();
        }

        return historyCache.getIfPresent( sessionId );
    }

    @Override
    public void cleanHistory(String sessionId) {
        historyCache.invalidate(sessionId);
    }
}
