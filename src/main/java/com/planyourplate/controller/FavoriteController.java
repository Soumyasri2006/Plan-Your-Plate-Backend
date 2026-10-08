package com.planyourplate.controller;

import com.planyourplate.entity.Favorite;
import com.planyourplate.entity.Recipe;
import com.planyourplate.repository.FavoriteRepository;
import com.planyourplate.repository.RecipeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final RecipeRepository recipeRepository;

    public FavoriteController(
            FavoriteRepository favoriteRepository,
            RecipeRepository recipeRepository) {

        this.favoriteRepository = favoriteRepository;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping
    public List<Favorite> getFavorites() {
        return favoriteRepository.findAll();
    }

    @PostMapping("/{recipeId}")
    public Favorite addFavorite(@PathVariable Long recipeId) {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Favorite favorite = new Favorite();
        favorite.setRecipe(recipe);

        return favoriteRepository.save(favorite);
    }

    @DeleteMapping("/{id}")
    public void deleteFavorite(@PathVariable Long id) {
        favoriteRepository.deleteById(id);
    }
}
