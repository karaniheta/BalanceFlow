package com.balanceflow.balanceflow_backend.category.service;

import com.balanceflow.balanceflow_backend.category.dto.CreateCategoryRequest;
import com.balanceflow.balanceflow_backend.category.dto.DeleteResponse;
import com.balanceflow.balanceflow_backend.category.entity.Category;
import com.balanceflow.balanceflow_backend.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category create(CreateCategoryRequest request) {

        Category category = Category.builder()
                .name(request.getName())
                .type(request.getType())
                .build();

        return categoryRepository.save(category);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category update(
            UUID id,
            CreateCategoryRequest request
    ) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());
        category.setType(request.getType());

        return categoryRepository.save(category);
    }

    public DeleteResponse delete(UUID id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryRepository.delete(category);

        return new DeleteResponse("Category deleted successfully");
    }
}