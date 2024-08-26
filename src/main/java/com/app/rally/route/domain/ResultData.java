package com.app.rally.route.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResultData <T>{
   private final ResponseType type;
    private final T body;
}
