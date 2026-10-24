
package com.planyourplate.repository;

import com.planyourplate.entity.Favorite;
import com.planyourplate.entity.Recipe;
import com.planyourplate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUser(User user);

    Optional<Favorite> findByUserAndRecipe(User user, Recipe recipe);
}

