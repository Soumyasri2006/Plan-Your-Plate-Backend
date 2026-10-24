
package com.planyourplate.controller;

import com.planyourplate.entity.MealPlan;
import com.planyourplate.entity.Recipe;
import com.planyourplate.entity.User;
import com.planyourplate.repository.MealPlanRepository;
import com.planyourplate.repository.RecipeRepository;
import com.planyourplate.repository.UserRepository;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/meal-plans")
public class MealPlanController {

    private final MealPlanRepository mealPlanRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public MealPlanController(
            MealPlanRepository mealPlanRepository,
            RecipeRepository recipeRepository,
            UserRepository userRepository) {
        this.mealPlanRepository = mealPlanRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<MealPlan> getMealPlans(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
            Authentication authentication) {

        if (endDate.isBefore(startDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date must not be before start date");
        }

        User user = getAuthenticatedUser(authentication);

        return mealPlanRepository
                .findByUserAndPlanDateBetweenOrderByPlanDateAsc(
                        user, startDate, endDate);
    }

    @PostMapping
    public MealPlan addMealPlan(
            @RequestParam Long recipeId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate planDate,
            @RequestParam String mealType,
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        String normalizedMealType = mealType.trim().toLowerCase();

        if (!List.of("breakfast", "lunch", "dinner").contains(normalizedMealType)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Meal type must be breakfast, lunch, or dinner");
        }

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Recipe not found"));

        MealPlan mealPlan = new MealPlan();
        mealPlan.setUser(user);
        mealPlan.setRecipe(recipe);
        mealPlan.setPlanDate(planDate);
        mealPlan.setMealType(normalizedMealType);

        return mealPlanRepository.save(mealPlan);
    }

    @DeleteMapping("/{id}")
    public void deleteMealPlan(
            @PathVariable Long id,
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        MealPlan mealPlan = mealPlanRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Meal plan not found"));

        mealPlanRepository.delete(mealPlan);
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

