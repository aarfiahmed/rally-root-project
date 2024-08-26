package com.app.rally.route.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public  class ClubReviewRequest {
    private String clubId;
    private String review;

}
