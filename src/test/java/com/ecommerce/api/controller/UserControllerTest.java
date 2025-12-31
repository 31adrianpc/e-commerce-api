package com.ecommerce.api.controller;

import com.ecommerce.api.config.SecurityConfig;
import com.ecommerce.api.dto.response.UserResponseDTO;
import com.ecommerce.api.security.CustomUserDetailsService;
import com.ecommerce.api.security.JwtAuthenticationFilter;
import com.ecommerce.api.service.JwtService;
import com.ecommerce.api.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.ecommerce.api.entity.UserEntity.USER_ROLE;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.List;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() throws ServletException, IOException {
        SecurityContextHolder.clearContext();

        // Configurar el filtro mock para que pase la cadena de filtros
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listAll_AsAdmin_ShouldReturnAllUsers() throws Exception{

        List<UserResponseDTO> users = List.of(
                UserResponseDTO.builder()
                        .id(1L)
                        .username("admin")
                        .email("admin@test.com")
                        .role(USER_ROLE.ADMIN)
                        .active(true)
                        .build(),
                UserResponseDTO.builder()
                        .id(2L)
                        .username("customer")
                        .email("customer@test.com")
                        .role(USER_ROLE.CUSTOMER)
                        .active(true)
                        .build()
        );

        when(userService.getAllUsers()).thenReturn(users);

        var result = mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[1].username").value("customer"))
                .andReturn();

        System.out.println("Status: " + result.getResponse().getStatus());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void listAll_AsCustomer_ShouldReturnForbidden() throws Exception{

        var result = mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isForbidden())
                .andReturn();

        System.out.println("Status: " + result.getResponse().getStatus());
        verify(userService, never()).getAllUsers();
    }

    @Test
    void listAll_WithoutAuth_ShouldReturnUnauthorized() throws Exception {

        var result = mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized())
                .andReturn();

        System.out.println("Status: " + result.getResponse().getStatus());
        verify(userService, never()).getAllUsers();
    }
}
