package com.planyourplate.controller;

import com.planyourplate.entity.RecipeIngredient;
import com.planyourplate.repository.RecipeIngredientRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/recipe-ingredients")
public class RecipeIngredientController {

    private final RecipeIngredientRepository repository;

    public RecipeIngredientController(RecipeIngredientRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<RecipeIngredient> getAllRecipeIngredients() {
        return repository.findAll();
    }

    @PostMapping
    public RecipeIngredient addRecipeIngredient(
            @RequestBody RecipeIngredient recipeIngredient) {
        return repository.save(recipeIngredient);
    }
}
