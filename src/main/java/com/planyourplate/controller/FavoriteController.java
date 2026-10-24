
package com.planyourplate.controller;

import com.planyourplate.entity.Favorite;
import com.planyourplate.entity.Recipe;
import com.planyourplate.entity.User;
import com.planyourplate.repository.FavoriteRepository;
import com.planyourplate.repository.RecipeRepository;
import com.planyourplate.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public FavoriteController(
            FavoriteRepository favoriteRepository,
            RecipeRepository recipeRepository,
            UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Favorite> getFavorites(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return favoriteRepository.findByUser(user);
    }

    @PostMapping("/{recipeId}")
    public Favorite addFavorite(
            @PathVariable Long recipeId,
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Recipe not found"));

        return favoriteRepository.findByUserAndRecipe(user, recipe)
                .orElseGet(() -> {
                    Favorite favorite = new Favorite();
                    favorite.setRecipe(recipe);
                    favorite.setUser(user);
                    return favoriteRepository.save(favorite);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteFavorite(
            @PathVariable Long id,
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Favorite not found"));

        if (favorite.getUser() == null
                || !favorite.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot delete another user's favorite");
        }

        favoriteRepository.delete(favorite);
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Please log in first");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not found"));
    }
}

