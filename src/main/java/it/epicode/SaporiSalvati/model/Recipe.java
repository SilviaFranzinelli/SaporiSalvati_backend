package it.epicode.SaporiSalvati.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "recipes")
public class Recipe {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String imageUrl;

    @Getter
    @Setter
    private String ingredients;

    @Getter
    @Setter
    private String instructions;

    @Getter
    @Setter
    private String category;

    @Getter
    @Setter
    public boolean favorite;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    private User user;


    @ManyToMany(mappedBy = "favoriteRecipes")
    private Set<User> usersWhoFavorited = new HashSet<>();



}

