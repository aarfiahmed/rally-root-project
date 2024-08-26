package com.app.rally.route.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException{
    private final String code;

    public UserException(String code,String msg){
        super(msg);
        this.code=code;
    }

}
