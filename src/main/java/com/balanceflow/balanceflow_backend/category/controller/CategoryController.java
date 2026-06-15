package com.balanceflow.balanceflow_backend.category.controller;

import com.balanceflow.balanceflow_backend.category.dto.CreateCategoryRequest;
import com.balanceflow.balanceflow_backend.category.dto.DeleteResponse;
import com.balanceflow.balanceflow_backend.category.entity.Category;
import com.balanceflow.balanceflow_backend.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public Category create(
            @RequestBody CreateCategoryRequest request
    ) {
        return categoryService.create(request);
    }

    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    @PutMapping("/{id}")
    public Category update(
            @PathVariable UUID id,
            @RequestBody CreateCategoryRequest request
    ) {
        return categoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public DeleteResponse delete(
            @PathVariable UUID id
    ) {
        return categoryService.delete(id);
    }
}