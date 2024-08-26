package com.app.rally.route.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="tournament")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="tournament_id")
    private Long id;
    private String tournamentName;
    private String type;
    private LocalDateTime tournamentDate;
    private String result;
    private String tournamentDescription;
}
