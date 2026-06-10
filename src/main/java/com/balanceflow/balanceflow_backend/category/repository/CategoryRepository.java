package com.balanceflow.balanceflow_backend.category.repository;

import com.balanceflow.balanceflow_backend.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}