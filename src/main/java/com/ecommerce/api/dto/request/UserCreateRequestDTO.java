package com.ecommerce.api.dto.request;

import com.ecommerce.api.entity.UserEntity.USER_ROLE;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserCreateRequestDTO extends UserBaseDTO{
    @NotNull(message = "Role es obligatorio")
    private USER_ROLE role;
}
