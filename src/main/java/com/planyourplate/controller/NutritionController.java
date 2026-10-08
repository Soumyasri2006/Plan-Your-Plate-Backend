package com.planyourplate.controller;

import com.planyourplate.dto.NutritionResponse;
import com.planyourplate.entity.RecipeIngredient;
import com.planyourplate.repository.RecipeIngredientRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

    private final RecipeIngredientRepository recipeIngredientRepository;

    public NutritionController(
            RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    @GetMapping("/{recipeId}")
    public NutritionResponse getNutrition(
            @PathVariable Long recipeId) {

        List<RecipeIngredient> ingredients =
                recipeIngredientRepository.findAll()
                        .stream()
                        .filter(ri -> ri.getRecipe().getId().equals(recipeId))
                        .toList();

        double calories = 0;
        double protein = 0;
        double carbohydrates = 0;
        double fat = 0;
        double fiber = 0;

        for (RecipeIngredient ri : ingredients) {

            double quantity = ri.getQuantity();

            calories += ri.getIngredient().getCalories() * quantity / 100;
            protein += ri.getIngredient().getProtein() * quantity / 100;
            carbohydrates += ri.getIngredient().getCarbohydrates() * quantity / 100;
            fat += ri.getIngredient().getFat() * quantity / 100;
            fiber += ri.getIngredient().getFiber() * quantity / 100;
        }

        int servings = ingredients.isEmpty()
                ? 1
                : ingredients.get(0).getRecipe().getServings();

        return new NutritionResponse(
                calories / servings,
                protein / servings,
                carbohydrates / servings,
                fat / servings,
                fiber / servings
        );
    }
}
