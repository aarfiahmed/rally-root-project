package com.app.rally.route.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class Response<T> {
     public final ResultData<T> data;
}
