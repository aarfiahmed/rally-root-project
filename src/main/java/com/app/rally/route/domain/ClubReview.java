package com.app.rally.route.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ClubReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_id")
    private Long id;
    private String review;
    private String reviewerEmail;
    private String reviewerFirstName;
}
