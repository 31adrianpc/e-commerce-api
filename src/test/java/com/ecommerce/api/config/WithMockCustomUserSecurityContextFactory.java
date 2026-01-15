package com.ecommerce.api.config;

import com.ecommerce.api.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        // Pasamos roles (string : "CUSTOMER") a authorities (GrantedAuthority : "ROLE_CUSTOMER")
        var authorities = Arrays.stream(annotation.roles())
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        // Crear principal a guardar en el contexto
        CustomUserDetails principal = new CustomUserDetails(
                annotation.id(),
                annotation.username(),
                annotation.password(),
                authorities,
                annotation.enabled()
        );

        // Crear authentication
        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal,
                principal.getPassword(),
                principal.getAuthorities()
        );

        // Crear contexto y guardar el authentication
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);

        return context;
    }
}
