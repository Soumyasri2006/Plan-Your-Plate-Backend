
package com.planyourplate.controller;

import com.planyourplate.entity.ShoppingList;
import com.planyourplate.entity.User;
import com.planyourplate.repository.ShoppingListRepository;
import com.planyourplate.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/shopping-list")
public class ShoppingListController {

    private final ShoppingListRepository shoppingListRepository;
    private final UserRepository userRepository;

    public ShoppingListController(
            ShoppingListRepository shoppingListRepository,
            UserRepository userRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<ShoppingList> getShoppingList(
            Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return shoppingListRepository.findByUser(user);
    }

    @PostMapping
    public ShoppingList addItem(
            @RequestBody ShoppingList item,
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        if (item.getIngredientName() == null
                || item.getIngredientName().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ingredient name is required");
        }

        String ingredientName = item.getIngredientName().trim();

        return shoppingListRepository
                .findByUserAndIngredientNameIgnoreCase(user, ingredientName)
                .orElseGet(() -> {
                    item.setId(null);
                    item.setIngredientName(ingredientName);
                    item.setUser(user);
                    item.setQuantity(0);
                    item.setUnit(null);

                    return shoppingListRepository.save(item);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteItem(
            @PathVariable Long id,
            Authentication authentication) {

        User user = getAuthenticatedUser(authentication);

        ShoppingList item = shoppingListRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Shopping-list item not found"));

        if (item.getUser() == null
                || !item.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot delete another user's shopping-list item");
        }

        shoppingListRepository.delete(item);
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

