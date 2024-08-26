package com.app.rally.route.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class ServiceError<T> extends Response<T>{
    public ServiceError(T data) {
        super(new ResultData<>(ResponseType.ERROR,data));
    }
}
