package com.nurturing.Service;

import com.nurturing.entity.ChatSentence;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

public interface ChatService {

    void setSessionId(Long userId, String sessionId);

    String getSessionId(Long userId);

    void deleteSessionId(Long userId);

    void setQuestionHistory(String sessionId, String question);

    void setAnswerHistory(String sessionId, String answer);

    List<ChatSentence> getHistory(String sessionId);

    void cleanHistory(String sessionId);

    SseEmitter queryStream(String sessionId, String query) throws IOException;
}
