package com.ecommerce.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserBaseDTO {
    @NotBlank(message = "Username es obligatorio")
    @Size(min = 3, max = 50, message = "Username debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "Email es obligatorio")
    @Email(message = "Email debe ser válido")
    @Size(max = 100, message = "Email no debe exceder 100 caracteres")
    private String email;

    @NotBlank(message = "Password es obligatorio")
    @Size(min = 8, message = "Password debe tener al menos 8 caracteres")
    private String password;

    @NotBlank(message = "Nombre es obligatorio")
    @Size(max = 50, message = "Nombre no debe exceder 50 caracteres")
    private String firstName;

    @NotBlank(message = "Apellido es obligatorio")
    @Size(max = 50, message = "Apellido no debe exceder 50 caracteres")
    private String lastName;
}
