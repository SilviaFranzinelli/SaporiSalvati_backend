package it.epicode.SaporiSalvati.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.epicode.SaporiSalvati.model.Recipe;
import it.epicode.SaporiSalvati.repository.RecipeRepository;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;


    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipe(Long id, Recipe recipe) {
        Optional<Recipe> optional = recipeRepository.findById(id);
        if (optional.isPresent()) {
            Recipe existingRecipe = optional.get();
            existingRecipe.setTitle(recipe.getTitle());
            existingRecipe.setIngredients(recipe.getIngredients());
            existingRecipe.setInstructions(recipe.getInstructions());
            existingRecipe.setImageUrl(recipe.getImageUrl());

            return recipeRepository.save(existingRecipe);
        }
        throw new RuntimeException("Ricetta non trovata");
    }

    public void deleteRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ricetta non trovata"));

        recipeRepository.deleteById(id);
    }

    public Recipe getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId).orElse(null);
    }
}
