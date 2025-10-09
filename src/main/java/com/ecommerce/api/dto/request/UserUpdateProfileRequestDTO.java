package com.ecommerce.api.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateProfileRequestDTO {

    @Size(max = 50, message = "Nombre no debe exceder 50 caracteres")
    private String firstName;

    @Size(max = 50, message = "Apellido no debe exceder 50 caracteres")
    private String lastName;

    @Size(min = 8, message = "Password debe tener al menos 8 caracteres")
    private String password;
}
