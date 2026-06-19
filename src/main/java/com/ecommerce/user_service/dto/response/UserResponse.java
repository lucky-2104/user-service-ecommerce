package com.ecommerce.user_service.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ecommerce.user_service.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private UUID id;
    private String fullName;
    private String email;
    private Role role;
    private boolean isActive;
    private LocalDateTime createdAt;
}