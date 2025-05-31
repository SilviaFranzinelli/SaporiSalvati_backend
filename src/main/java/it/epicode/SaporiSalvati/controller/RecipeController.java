package it.epicode.SaporiSalvati.controller;

import it.epicode.SaporiSalvati.model.Recipe;
import it.epicode.SaporiSalvati.repository.UserRepository;
import it.epicode.SaporiSalvati.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    private String getCurrentUsername() { // Metodo helper
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new SecurityException("User not authenticated");
        }
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    @PostMapping
    public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe) {
        String username = getCurrentUsername();
        return ResponseEntity.ok(recipeService.saveRecipe(recipe));
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        String username = getCurrentUsername();
        return ResponseEntity.ok(recipeService.getRecipesByUser(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe updatedRecipe) {
        return recipeService.getRecipeById(id)
                .map(recipe -> {
                    recipe.setTitle(updatedRecipe.getTitle());
                    recipe.setIngredients(updatedRecipe.getIngredients());
                    recipe.setInstructions(updatedRecipe.getInstructions());
                    recipe.setCategory(updatedRecipe.getCategory());
                    recipe.setImageUrl(updatedRecipe.getImageUrl());
                    return ResponseEntity.ok(recipeService.saveRecipe(recipe));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id, getCurrentUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<Recipe> toggleFavorite(@PathVariable Long id) {
        Recipe recipe = recipeService.toggleFavorite(id, getCurrentUsername());
        if (recipe != null) {
            return ResponseEntity.ok(recipe);
        }
        return ResponseEntity.notFound().build();
    }
}