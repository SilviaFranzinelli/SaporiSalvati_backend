package it.epicode.SaporiSalvati.service;

import it.epicode.SaporiSalvati.model.Recipe;
import it.epicode.SaporiSalvati.model.User; // AGGIUNTO
import it.epicode.SaporiSalvati.repository.RecipeRepository;
import it.epicode.SaporiSalvati.repository.UserRepository; // AGGIUNTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder; // AGGIUNTO
import org.springframework.security.core.userdetails.UserDetails; // AGGIUNTO
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Per operazioni multiple

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Recipe saveRecipe(Recipe recipe) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
        recipe.setUser(user);
        return recipeRepository.save(recipe);
    }

    public Recipe internalSaveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }


    public List<Recipe> getRecipesByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return recipeRepository.findByUser(user);
    }


    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    @Transactional
    public void deleteRecipe(Long id, String username) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        if (!recipe.getUser().getUsername().equals(username)) {
            throw new SecurityException("User not authorized to delete this recipe");
        }
        recipeRepository.deleteById(id);
    }

    @Transactional
    public Recipe toggleFavorite(Long id, String username) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isPresent()) {
            Recipe recipe = optionalRecipe.get();
            recipe.setFavorite(!recipe.isFavorite());
            return recipeRepository.save(recipe);
        }
        return null;
    }

    @Transactional
    public Recipe updateRecipe(Long id, Recipe updatedRecipeData, String username) {
        Recipe existingRecipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id: " + id));

        if (!existingRecipe.getUser().getUsername().equals(username)) {
            throw new SecurityException("User not authorized to update this recipe");
        }

        existingRecipe.setTitle(updatedRecipeData.getTitle());
        existingRecipe.setIngredients(updatedRecipeData.getIngredients());
        existingRecipe.setInstructions(updatedRecipeData.getInstructions());
        existingRecipe.setCategory(updatedRecipeData.getCategory());
        existingRecipe.setImageUrl(updatedRecipeData.getImageUrl());


        return recipeRepository.save(existingRecipe);
    }
}