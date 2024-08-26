package com.app.rally.route.exception;

public class CluBException extends RuntimeException{
    private final String code;
    public CluBException(String code,String message){
        super(message);
        this.code=code;
    }

    public String getCode() {
        return code;
    }
}
