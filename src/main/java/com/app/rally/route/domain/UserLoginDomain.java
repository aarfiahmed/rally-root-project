package com.app.rally.route.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserLoginDomain {
    private String userName;
    private String password;
}
