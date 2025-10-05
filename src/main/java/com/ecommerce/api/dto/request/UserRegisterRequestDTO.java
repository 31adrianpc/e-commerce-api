package com.ecommerce.api.dto.request;

import com.ecommerce.api.entity.UserEntity.USER_ROLE;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterRequestDTO {
    
    @NotBlank(message = "Email es obligatorio")
    @Email(message = "Email debe ser válido")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "Password es obligatorio")
    @Size(min = 8, message = "Password debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "Username es obligatorio")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Nombre es obligatorio")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Apellido es obligatorio")
    @Size(max = 50)
    private String lastName;

    private USER_ROLE role;
}
