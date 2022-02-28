package com.example.instagramproject.util.pic_purify.status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {

    private int errorCode;
    private String errorMsg;

}
