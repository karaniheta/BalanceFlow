package com.balanceflow.balanceflow_backend.group.dto;

import lombok.Data;

@Data
public class CreateGroupRequest {

    private String name;

    private String description;
}