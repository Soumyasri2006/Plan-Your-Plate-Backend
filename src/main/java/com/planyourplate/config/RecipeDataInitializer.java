
package com.planyourplate.config;

import com.planyourplate.entity.Recipe;
import com.planyourplate.repository.RecipeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecipeDataInitializer {

    @Bean
    CommandLineRunner initializeRecipes(RecipeRepository recipeRepository) {
        return args -> {
            addRecipeIfMissing(recipeRepository,
                    "Oats Vegetable Bowl",
                    "A healthy bowl of oats with fresh vegetables.",
                    10, 15, 2, "Easy", "Vegetarian",
                    "1. Cook oats. 2. Add cooked vegetables. 3. Season and serve.");

            addRecipeIfMissing(recipeRepository,
                    "Tomato Cheese Pasta",
                    "A comforting pasta with tomato sauce and cheese.",
                    10, 20, 2, "Easy", "Vegetarian",
                    "1. Boil pasta. 2. Prepare tomato sauce. 3. Mix pasta with sauce and cheese.");

            addRecipeIfMissing(recipeRepository,
                    "Peanut Butter Banana Toast",
                    "Quick toast topped with peanut butter and banana.",
                    5, 5, 1, "Easy", "Vegetarian",
                    "1. Toast bread. 2. Spread peanut butter. 3. Add banana slices.");

            addRecipeIfMissing(recipeRepository,
                    "Lemon Chickpea Rice",
                    "Rice mixed with chickpeas and a fresh lemon flavour.",
                    10, 20, 2, "Easy", "Vegan",
                    "1. Cook rice. 2. Add cooked chickpeas and seasoning. 3. Finish with lemon juice.");

            addRecipeIfMissing(recipeRepository,
                    "Vegetable Omelette",
                    "An omelette with onion, tomato and capsicum.",
                    10, 10, 1, "Easy", "Non-Vegetarian",
                    "1. Beat eggs. 2. Add chopped vegetables. 3. Cook in a lightly oiled pan.");

            System.out.println("Recipe data initialization completed.");
        };
    }

    private void addRecipeIfMissing(
            RecipeRepository repository,
            String name,
            String description,
            int preparationTime,
            int cookingTime,
            int servings,
            String difficulty,
            String dietaryPreference,
            String instructions) {

        boolean exists = repository.findAll().stream()
                .anyMatch(recipe -> recipe.getName() != null
                        && recipe.getName().equalsIgnoreCase(name));

        if (!exists) {
            Recipe recipe = new Recipe();
            recipe.setName(name);
            recipe.setDescription(description);
            recipe.setPreparationTime(preparationTime);
            recipe.setCookingTime(cookingTime);
            recipe.setServings(servings);
            recipe.setDifficulty(difficulty);
            recipe.setDietaryPreference(dietaryPreference);
            recipe.setInstructions(instructions);

            repository.save(recipe);
            System.out.println("Added starter recipe: " + name);
        }
    }
}

