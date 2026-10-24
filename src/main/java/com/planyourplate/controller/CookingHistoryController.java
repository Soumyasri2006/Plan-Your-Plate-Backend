
package com.planyourplate.controller;

import com.planyourplate.entity.CookingHistory;
import com.planyourplate.entity.Recipe;
import com.planyourplate.entity.User;
import com.planyourplate.repository.CookingHistoryRepository;
import com.planyourplate.repository.RecipeRepository;
import com.planyourplate.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cooking-history")
public class CookingHistoryController {

    private final CookingHistoryRepository cookingHistoryRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public CookingHistoryController(
            CookingHistoryRepository cookingHistoryRepository,
            RecipeRepository recipeRepository,
            UserRepository userRepository) {
        this.cookingHistoryRepository = cookingHistoryRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public CookingHistory markRecipeAsCooked(
            @RequestParam Long recipeId,
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Recipe not found"));

        CookingHistory history = new CookingHistory();
        history.setUser(user);
        history.setRecipe(recipe);
        history.setCookedAt(LocalDateTime.now());

        return cookingHistoryRepository.save(history);
    }

    @GetMapping
    public List<CookingHistory> getCookingHistory(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return cookingHistoryRepository.findByUserOrderByCookedAtDesc(user);
    }

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Please log in first");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "User not found"));
    }
}

