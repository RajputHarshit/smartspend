package com.harshit.smartspend.repository;

import com.harshit.smartspend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    boolean existsByName(String name);
}
