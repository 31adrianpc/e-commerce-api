package com.ecommerce.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.ecommerce.api.dto.request.UserRegisterRequestDTO;
import com.ecommerce.api.dto.request.UserUpdateProfileRequestDTO;
import com.ecommerce.api.dto.request.UserUpdateRequestDTO;
import com.ecommerce.api.dto.response.UserResponseDTO;
import com.ecommerce.api.entity.UserEntity;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    
    UserResponseDTO toResponseDTO(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "active", ignore = true)
    UserEntity toEntity(UserRegisterRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateProfileFromDTO(UserUpdateProfileRequestDTO dto, @MappingTarget UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "orders", ignore = true)
    void updateEntityFromDTO(UserUpdateRequestDTO dto, @MappingTarget UserEntity entity);
}
