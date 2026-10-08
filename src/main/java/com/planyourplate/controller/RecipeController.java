package com.planyourplate.controller;

import com.planyourplate.entity.Recipe;
import com.planyourplate.repository.RecipeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }
    @GetMapping("/{id}")
public Recipe getRecipeById(@PathVariable Long id) {
    return recipeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Recipe not found"));
}

    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        return recipeRepository.save(recipe);
    }
}
