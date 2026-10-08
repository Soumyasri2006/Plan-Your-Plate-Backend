
package com.planyourplate.controller;

import com.planyourplate.entity.Recipe;
import com.planyourplate.entity.RecipeIngredient;
import com.planyourplate.repository.RecipeIngredientRepository;
import com.planyourplate.repository.RecipeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/recommendations")
public class RecipeRecommendationController {

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    public RecipeRecommendationController(
            RecipeRepository recipeRepository,
            RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    @GetMapping
    public List<Map<String, Object>> getRecommendations(
            @RequestParam List<String> ingredients) {

        Set<String> available = new HashSet<>();
        for (String name : ingredients) {
            available.add(name.trim().toLowerCase());
        }

        List<Map<String, Object>> results = new ArrayList<>();

        for (Recipe recipe : recipeRepository.findAll()) {
            List<RecipeIngredient> required =
                    recipeIngredientRepository.findAll().stream()
                            .filter(ri -> ri.getRecipe().getId()
                                    .equals(recipe.getId()))
                            .toList();

            if (required.isEmpty()) {
                continue;
            }

            List<String> missing = new ArrayList<>();
            int matched = 0;

            for (RecipeIngredient ri : required) {
                String name = ri.getIngredient().getName()
                        .toLowerCase();

                if (available.contains(name)) {
                    matched++;
                } else {
                    missing.add(ri.getIngredient().getName());
                }
            }

            double percentage =
                    (matched * 100.0) / required.size();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("recipeId", recipe.getId());
            result.put("recipeName", recipe.getName());
            result.put("matchPercentage",
                    Math.round(percentage));
            result.put("missingIngredients", missing);

            results.add(result);
        }

        results.sort((a, b) ->
                Double.compare(
                        ((Number) b.get("matchPercentage")).doubleValue(),
                        ((Number) a.get("matchPercentage")).doubleValue()));

        return results;
    }
}
