package com.planyourplate.repository;

import com.planyourplate.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository
        extends JpaRepository<RecipeIngredient, Long> {

}
