package com.app.rally.route.entity;

import com.app.rally.route.domain.ClubReview;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="club")
@Data
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="club_id")
    private Long id;
    private String name;
    private String shortAddress;
    private String fullAddress;
    private String country;
    private String state;
    private String city;
    @Column(length = 100000)
    private String description;
    private String latitude;
    private String longitude;
    private String contact;
    private String email;
    private String clubImage;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ClubReview> reviews;


    @OneToMany(cascade = CascadeType.ALL)
    List<Tournament> tournaments;

}
