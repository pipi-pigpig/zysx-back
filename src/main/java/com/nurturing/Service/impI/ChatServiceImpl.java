package com.nurturing.Service.impI;

import com.nurturing.Service.ChatService;
import com.nurturing.chat.LLMClient;
import com.nurturing.chat.store.ChatHistoryStore;
import com.nurturing.entity.ChatSentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatHistoryStore chatHistoryStore;

    @Autowired
    private LLMClient LLMClient;

    @Override
    public void setSessionId(Long userId, String sessionId) {

    }

    @Override
    public String getSessionId(Long userId) {
        return "";
    }


    @Override
    public void deleteSessionId(Long userId) {

    }

    @Override
    public void setQuestionHistory(String sessionId, String question) {
        chatHistoryStore.saveQuestionHistory(sessionId,question);
    }

    @Override
    public void setAnswerHistory(String sessionId, String answer) {
        chatHistoryStore.saveAnswerHistory(sessionId,answer);
    }

    @Override
    public List<ChatSentence> getHistory(String sessionId) {
        return chatHistoryStore.getHistory(sessionId);
    }

    @Override
    public void cleanHistory(String sessionId) {
        System.out.println("clean history : "+sessionId);
        chatHistoryStore.cleanHistory(sessionId);
    }



    @Override
    public SseEmitter queryStream(String sessionId, String query) throws IOException {

        if(sessionId==null || sessionId.isEmpty()){
            sessionId= "query:"+UUID.randomUUID().toString();
        }

        ChatSentence question =new ChatSentence("user",query);
        List<ChatSentence> history = chatHistoryStore.getHistory(sessionId);
        System.out.println("get history : "+sessionId);
        System.out.println("history:"+history.toString());

        history.add(question);

        return LLMClient.streamChat(sessionId, history);
    }
}
