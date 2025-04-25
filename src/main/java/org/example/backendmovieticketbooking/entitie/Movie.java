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
    @Version
    private Integer version;

    public Movie(String name, int yearOfRelease, String genre, String language, String thumbnailUrl) {
        this.name = name;
        this.yearOfRelease = yearOfRelease;
        this.genre = genre;
        this.language = language;
        this.thumbnailUrl = thumbnailUrl;
    }
}