package com.github.remering.scratch.springboot.message.account;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.message.CodeMessageResponse;
import lombok.Builder;
import lombok.Data;

public class Logout {
    private Logout() {
    }

    @Data
    @Builder
    public static class Response implements CodeMessageResponse {
        private final ResponseCode code;
        private final String message;
    }
}
