package com.github.remering.scratch.springboot.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum ResponseCode {
    SUCCESS,
    INFO,
    WARNING,
    ERROR,
}
