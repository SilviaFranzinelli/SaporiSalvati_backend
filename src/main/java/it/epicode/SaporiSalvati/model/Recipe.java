package it.epicode.SaporiSalvati.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    private String ingredients;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Getter
    @Setter
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    @JsonIgnore
    private User user;


    @ManyToMany(mappedBy = "favoriteRecipes")
    @JsonIgnore
    private Set<User> usersWhoFavorited = new HashSet<>();



}

