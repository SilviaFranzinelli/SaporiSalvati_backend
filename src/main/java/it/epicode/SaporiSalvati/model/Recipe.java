package it.epicode.SaporiSalvati.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Recipe {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String ingredients;

    @Lob
    private String instructions;

    private String category;

    private boolean favorite;

    private String imageUrl;
}
