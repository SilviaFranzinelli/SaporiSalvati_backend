package it.epicode.SaporiSalvati.controller;

import it.epicode.SaporiSalvati.model.Recipe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/parse")
public class ParsingController {

    @PostMapping
    public ResponseEntity<Recipe> parseRecipeFromUrl(@RequestParam String url) {
        try {

            Document doc = Jsoup.connect(url).get();
            Recipe recipe = new Recipe();

            if (url.contains("giallozafferano.it")) {
                recipe.setTitle(String.valueOf(doc.select("h1").first()));
                recipe.setIngredients(doc.select(".ingredienti, .gz-ingredient-list, .gz-ingredients-list").text());
                recipe.setInstructions(doc.select(".gz-steps-list, .preparazione, .gz-steps, .gz-preparation-steps").text());
                recipe.setImageUrl(Objects.requireNonNull(doc.select("img").first()).attr("src"));
                recipe.setCategory("Giallo Zafferano");
            } else if (url.contains("cucchiaio.it")) {
                recipe.setTitle(String.valueOf(doc.select("h1").first()));
                recipe.setIngredients(doc.select(".ingredienti, .scheda-ingredienti").text());
                recipe.setInstructions(doc.select(".preparazione, .scheda-preparazione").text());
                recipe.setImageUrl(Objects.requireNonNull(doc.select("img").first()).attr("src"));
                recipe.setCategory("Cucchiaio d'Argento");
            } else if (url.contains("fattoincasadabenedetta.it")) {
                recipe.setTitle(String.valueOf(doc.select("h1").first()));
                recipe.setIngredients(doc.select(".ingredienti, .ingredients-list").text());
                recipe.setInstructions(doc.select(".preparazione, .instructions").text());
                recipe.setImageUrl(Objects.requireNonNull(doc.select("img").first()).attr("src"));
                recipe.setCategory("Fatto in casa da Benedetta");
            } else {
                return ResponseEntity.badRequest().body(null);
            }

            recipe.setFavorite(false);

            return ResponseEntity.ok(recipe);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}