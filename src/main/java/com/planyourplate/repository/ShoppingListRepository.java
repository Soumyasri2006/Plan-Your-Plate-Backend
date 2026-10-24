
package com.planyourplate.repository;

import com.planyourplate.entity.ShoppingList;
import com.planyourplate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingListRepository
        extends JpaRepository<ShoppingList, Long> {

    List<ShoppingList> findByUser(User user);

    Optional<ShoppingList> findByUserAndIngredientNameIgnoreCase(
            User user, String ingredientName);
}


