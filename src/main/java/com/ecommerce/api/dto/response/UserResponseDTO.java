package com.ecommerce.api.dto.response;

import java.sql.Timestamp;

import com.ecommerce.api.entity.UserEntity.USER_ROLE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private USER_ROLE role;
    private boolean active;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
