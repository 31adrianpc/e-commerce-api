package com.ecommerce.api.security;

import com.ecommerce.api.entity.UserEntity;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        UserEntity user = findActiveUserByIdentifier(identifier);

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .disabled(!user.isActive())
                .build();
    }

    private UserEntity findActiveUserByIdentifier(String identifier) {
        if (identifier.contains("@")){
            return userRepository.findByEmailAndActive(identifier, true)
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontró ningun usuario activo con dicho email"));
        }
        else {
            return userRepository.findByUsernameAndActive(identifier, true)
                    .orElseThrow(() -> new ResourceNotFoundException("No se encontró ningun usuario activo con dicho username"));
        }
    }
}
