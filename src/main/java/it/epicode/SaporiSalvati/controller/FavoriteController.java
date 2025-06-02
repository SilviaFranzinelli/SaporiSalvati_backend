package it.epicode.SaporiSalvati.controller;

import it.epicode.SaporiSalvati.model.Recipe;
import it.epicode.SaporiSalvati.model.User;
import it.epicode.SaporiSalvati.service.RecipeService;
import it.epicode.SaporiSalvati.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/recipes/favorites")
public class FavoriteController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @PostMapping("/add")
    public ResponseEntity<?> addFavorite(@RequestParam Long recipeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userService.getUserByUsername(username);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (recipe == null) {
            return ResponseEntity.badRequest().body("Ricetta non trovata.");
        }

        user.getFavoriteRecipes().add(recipe);
        userService.save(user);

        return ResponseEntity.ok("Ricetta aggiunta ai preferiti.");
    }

    @GetMapping
    public List<Recipe> getFavoriteRecipes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userService.getUserByUsername(username);
        return new ArrayList<>(user.getFavoriteRecipes());
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFavorite(@RequestParam Long recipeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userService.getUserByUsername(username);
        Recipe recipe = recipeService.getRecipeById(recipeId);

        if (recipe == null) {
            return ResponseEntity.badRequest().body("Ricetta non trovata.");
        }

        user.getFavoriteRecipes().remove(recipe);
        userService.save(user);

        return ResponseEntity.ok("Ricetta rimossa dai preferiti.");
    }
}