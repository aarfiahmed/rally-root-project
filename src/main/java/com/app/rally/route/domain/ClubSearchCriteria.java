package com.app.rally.route.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClubSearchCriteria {

    private String country;
    private String state;
    private String city;

    private String latitude;
    private String longitude;
}
