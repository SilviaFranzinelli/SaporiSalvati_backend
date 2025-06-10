package it.epicode.SaporiSalvati.controller;

import it.epicode.SaporiSalvati.model.Recipe;
import it.epicode.SaporiSalvati.model.User;
import it.epicode.SaporiSalvati.service.RecipeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
                Element img1 = doc.select(".photo, .gz-featured-image .lazyload").first();
                recipe.setImageUrl(img1 != null ? img1.attr("src") : null);

            } else if (url.contains("cucchiaio.it")) {
                recipe.setTitle(doc.select("h1").text());
                recipe.setIngredients(doc.select("div[data-browserecipe-slide-ref='2'] .c-recipe__list2 ul li").text());
                recipe.setInstructions(doc.select("div.recipe_procedures.section div.mb-f30 p").text());
                Element img2 = doc.select("div[data-browserecipe-slide-ref='1.1'] img").first();
                recipe.setImageUrl(img2 != null ? img2.attr("src") : null);

            } else if (url.contains("fattoincasadabenedetta.it")) {
                recipe.setTitle(doc.select("h1").text());
                recipe.setIngredients(doc.select(".recipe-section .wrap .content .group ul li").text());
                recipe.setInstructions(doc.select(".recipe-main-section .recipe-section .steps .step .content span p").text());
                Element img3 = doc.select(".wp-block-image img").first();
                recipe.setImageUrl(img3 != null ? img3.attr("src") : null);
            } else {
                return ResponseEntity.badRequest().body(null);
            }


            recipeService.saveRecipe(recipe);


            return ResponseEntity.ok(recipe);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}