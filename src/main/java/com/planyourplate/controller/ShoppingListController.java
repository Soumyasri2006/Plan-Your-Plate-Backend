package com.planyourplate.controller;

import com.planyourplate.entity.ShoppingList;
import com.planyourplate.repository.ShoppingListRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/shopping-list")
public class ShoppingListController {

    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListController(
            ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    @GetMapping
    public List<ShoppingList> getShoppingList() {
        return shoppingListRepository.findAll();
    }

    @PostMapping
    public ShoppingList addItem(
            @RequestBody ShoppingList item) {

        return shoppingListRepository.save(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        shoppingListRepository.deleteById(id);
    }
}
