package com.ecommerce.api.security;

import com.ecommerce.api.entity.UserEntity;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        UserEntity user = findActiveUserByIdentifier(identifier);

        Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        return new CustomUserDetails(user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities,
                user.isActive());
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
