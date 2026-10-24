
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
                        .filter(ri ->
                                ri.getRecipe().getId().equals(recipeId))
                        .toList();

        double calories = 0;
        double protein = 0;
        double carbohydrates = 0;
        double fat = 0;
        double fiber = 0;

        
for (RecipeIngredient ri : ingredients) {

    double quantity = ri.getQuantity();
    String unit = ri.getUnit();

    // Convert ingredient quantity into grams.
    double quantityInGrams;

    if (unit != null &&
            (unit.equalsIgnoreCase("g")
                    || unit.equalsIgnoreCase("grams"))) {

        quantityInGrams = quantity;

    } else if (unit != null &&
            unit.equalsIgnoreCase("pieces")
            && ri.getIngredient().getName()
                    .equalsIgnoreCase("Egg")) {

        // Estimate one egg as 50 grams.
        quantityInGrams = quantity * 50;

    } else {
        throw new IllegalArgumentException(
                "Unsupported ingredient unit: " + unit);
    }

    double multiplier = quantityInGrams / 100.0;

    calories += ri.getIngredient().getCalories() * multiplier;
    protein += ri.getIngredient().getProtein() * multiplier;
    carbohydrates +=
            ri.getIngredient().getCarbohydrates() * multiplier;
    fat += ri.getIngredient().getFat() * multiplier;
    fiber += ri.getIngredient().getFiber() * multiplier;
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
