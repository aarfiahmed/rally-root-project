package com.app.rally.route.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfileDomain {
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private byte[] image;
}
