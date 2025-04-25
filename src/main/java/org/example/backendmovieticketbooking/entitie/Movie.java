package org.example.backendmovieticketbooking.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int yearOfRelease;
    private String genre;
    private String language;
    private String thumbnailUrl;
    private String backgroundPosterUrl;
    private String TrailerUrl;
    @Version
    private Integer version;

    public Movie(String name, int yearOfRelease, String genre, String language, String thumbnailUrl, String backgroundPosterUrl, String TrailerUrl) {
        this.name = name;
        this.yearOfRelease = yearOfRelease;
        this.genre = genre;
        this.language = language;
        this.thumbnailUrl = thumbnailUrl;
        this.backgroundPosterUrl = backgroundPosterUrl;
        this.TrailerUrl = TrailerUrl;
    }
}