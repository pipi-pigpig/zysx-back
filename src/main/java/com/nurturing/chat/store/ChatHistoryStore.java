package com.nurturing.chat.store;

import com.nurturing.entity.ChatSentence;

import java.util.List;
import java.util.Map;

public interface ChatHistoryStore {

    /**
     * 保存本次回答的问题，添加到本轮对话历史的末尾
     * @param sessionId
     * @param question
     * @param
     */
    void saveQuestionHistory(String sessionId, String question);

    /**
     * 保存本次回答的回答，添加到本轮对话历史的末尾
     * @param sessionId
     * @param answer
     * @param
     */
    void saveAnswerHistory(String sessionId, String answer);

    /**
     * 得到本轮对话的所有对话历史
     * @param sessionId
     * @return
     */
    List<ChatSentence> getHistory(String sessionId);

    /**
     * 移除某轮对话的所有对话历史
     * @param sessionId
     */
    void cleanHistory(String sessionId);
}
