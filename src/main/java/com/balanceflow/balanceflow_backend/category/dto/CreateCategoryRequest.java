package com.balanceflow.balanceflow_backend.category.dto;

import lombok.Data;

@Data
public class CreateCategoryRequest {

    private String name;

    private String type;
}