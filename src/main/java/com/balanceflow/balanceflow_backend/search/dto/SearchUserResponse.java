package com.balanceflow.balanceflow_backend.search.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class SearchUserResponse {
    private UUID id;
    private String fullName;
    private String email;
}
