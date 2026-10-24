
package com.planyourplate.repository;

import com.planyourplate.entity.MealPlan;
import com.planyourplate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {

    List<MealPlan> findByUserAndPlanDateBetweenOrderByPlanDateAsc(
            User user, LocalDate startDate, LocalDate endDate);

    Optional<MealPlan> findByIdAndUser(Long id, User user);
}
