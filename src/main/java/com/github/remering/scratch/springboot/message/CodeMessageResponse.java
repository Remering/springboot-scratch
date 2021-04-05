package com.github.remering.scratch.springboot.message;

import com.github.remering.scratch.springboot.bean.ResponseCode;
import com.github.remering.scratch.springboot.bean.ResponseMessage;

public interface CodeMessageResponse {
    ResponseCode getCode();
    String getMessage();
}
