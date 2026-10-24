
package com.planyourplate.repository;

import com.planyourplate.entity.CookingHistory;
import com.planyourplate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CookingHistoryRepository extends JpaRepository<CookingHistory, Long> {

    List<CookingHistory> findByUserOrderByCookedAtDesc(User user);
}

