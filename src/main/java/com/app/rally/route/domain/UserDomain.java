package com.app.rally.route.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public  class UserDomain {
    private String mobileNumber;
    private String address;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
}
