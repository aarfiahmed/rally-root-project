package com.app.rally.route.domain;

import lombok.Getter;

@Getter
public class Result<T> extends Response<T>{

    public Result(T data) {
        super(new ResultData<>(ResponseType.SUCCESS,data));
    }
}
