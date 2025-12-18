package com.nurturing.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatModelResponse {
    private String id;
    @JsonProperty("object")
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;

    @Data
    public static class Choice {
        private int index;
        private Delta delta;
        private String finishReason;

        @Data
        public static class Delta {
            private String content;

        }

    }
}
