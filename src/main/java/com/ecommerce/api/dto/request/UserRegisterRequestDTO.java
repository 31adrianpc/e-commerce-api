package com.ecommerce.api.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class UserRegisterRequestDTO extends UserBaseDTO{

}
