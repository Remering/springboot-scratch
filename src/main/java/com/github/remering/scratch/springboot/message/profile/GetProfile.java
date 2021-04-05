package com.github.remering.scratch.springboot.message.profile;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.bean.message.ProfileMessage;
import com.github.remering.scratch.springboot.message.CodeMessageResponse;
import lombok.Builder;
import lombok.Data;

public class GetProfile {

    private GetProfile() {
    }

    @Data
    @Builder
    public static class Response implements CodeMessageResponse {
        private final ResponseCode code;
        private final String message;
        @Builder.Default
        private final ProfileMessage profile = null;
    }
}
