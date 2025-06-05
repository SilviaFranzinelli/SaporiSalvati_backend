package it.epicode.SaporiSalvati.controller;

import it.epicode.SaporiSalvati.model.Recipe;
import it.epicode.SaporiSalvati.model.User;
import it.epicode.SaporiSalvati.service.RecipeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/parse")
public class ParsingController {
    @Autowired
    private RecipeService recipeService;

    @PostMapping
    public ResponseEntity<Recipe> parseRecipeFromUrl(@RequestParam String url, @AuthenticationPrincipal User user) {
        try {

            Document doc = Jsoup.connect(url).get();
            Recipe recipe = new Recipe();
            System.out.println(user);
            recipe.setUser(user);

            if (url.contains("ricette.giallozafferano.it")) {
                recipe.setTitle(doc.select("h1").text());
                recipe.setIngredients(doc.select(".gz-ingredient").text());
                recipe.setInstructions(doc.select(".gz-content-recipe-step p").text());
                recipe.setImageUrl(Objects.requireNonNull(doc.select(".gz-featured-image").first()).attr("src"));

            } else if (url.contains("cucchiaio.it")) {
                recipe.setTitle(doc.select("h1").text());
                recipe.setIngredients(doc.select("ul li").text());
                recipe.setInstructions(doc.select("div.recipe_procedures.section div.mb-f30 p").text());
                recipe.setImageUrl(Objects.requireNonNull(doc.select("picture img").first()).attr("src"));

            } else if (url.contains("fattoincasadabenedetta.it")) {
                recipe.setTitle(doc.select("h1").text());
                recipe.setIngredients(doc.select(".content group ul li").text());
                recipe.setInstructions(doc.select(".recipe-main-section recipe-section steps step content p").text());
                recipe.setImageUrl(Objects.requireNonNull(doc.select("img").first()).attr("src"));

            } else {
                return ResponseEntity.badRequest().body(null);
            }

            recipe.setFavorite(false);

            recipeService.saveRecipe(recipe);


            return ResponseEntity.ok(recipe);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}