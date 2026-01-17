package com.ecommerce.api.dto.request;

import com.ecommerce.api.entity.UserEntity.USER_ROLE;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true) // Para considerar tambien los campos del padre al usar equals()
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequestDTO extends UserBaseDTO{
    @NotNull(message = "Role es obligatorio")
    private USER_ROLE role;
}
