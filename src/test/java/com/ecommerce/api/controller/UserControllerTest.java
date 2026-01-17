package com.ecommerce.api.controller;

import com.ecommerce.api.config.SecurityConfig;
import com.ecommerce.api.config.WithMockCustomUser;
import com.ecommerce.api.dto.request.UserCreateRequestDTO;
import com.ecommerce.api.dto.response.UserResponseDTO;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.security.CustomUserDetailsService;
import com.ecommerce.api.security.JwtAuthenticationFilter;
import com.ecommerce.api.service.JwtService;
import com.ecommerce.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.List;

@WebMvcTest(controllers = UserController.class) // Queremos probar UserController
@Import(SecurityConfig.class) // Importamos nuestra clase SecurityConfig del main
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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws ServletException, IOException {
        SecurityContextHolder.clearContext();

        // Configurar el filtro mock para que pase la cadena de filtros
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response); // No hacemos nada, simplemente pasamos al siguiente filtro
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any()); // para cualquier request, response y chain
    }

    /* ------ LIST ALL ------ */
    @Test
    @WithMockUser(roles = "ADMIN") // Utilizamos dicha anotación para inyectar directamente un authentication al securityContext
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

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[1].username").value("customer"));

        verify(userService).getAllUsers();
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void listAll_AsCustomer_ShouldReturnForbidden() throws Exception{

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isForbidden());

        verify(userService, never()).getAllUsers();
    }

    @Test
    void listAll_WithoutAuth_ShouldReturnUnauthorized() throws Exception {

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).getAllUsers();
    }

    /* ------ GET BY ID ------ */
    @Test
    @WithMockUser(roles = "ADMIN")
    void getById_AsAdmin_ShouldReturnUser() throws Exception {
        Long userId = 1L;

        UserResponseDTO user = UserResponseDTO.builder()
                .id(userId)
                .username("admin")
                .email("admin@example.com")
                .role(USER_ROLE.ADMIN)
                .active(true)
                .build();

        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));

        verify(userService).getUserById(userId);
    }

    @Test
    @WithMockCustomUser(id = 1, roles = {"CUSTOMER"})
    void getById_AsCustomer_WithDifferentId_ShouldReturnForbidden() throws Exception{
        Long userId = 2L;

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isForbidden());

        verify(userService, never()).getUserById(any());
    }

    @Test
    @WithMockCustomUser(id = 1, username = "customer", roles = {"CUSTOMER"})
    void getById_AsCustomer_WithSameId_ShouldReturnUser() throws Exception{
        Long userId = 1L;

        UserResponseDTO user = UserResponseDTO.builder()
                .id(userId)
                .username("customer")
                .email("customer@example.com")
                .role(USER_ROLE.CUSTOMER)
                .active(true)
                .build();

        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("customer"));

        verify(userService).getUserById(userId);
    }

    @Test
    void getById_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        Long userId = 1L;

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).getUserById(userId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        Long userId = 999L;

        when(userService.getUserById(userId)).thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(userId);
    }

    /* ------ DELETE BY ID ------ */
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteById_AsAdmin_ShouldReturnNoContent() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(userId);
    }

    @Test
    @WithMockCustomUser(id = 1, roles = {"CUSTOMER"})
    void deleteById_AsCustomer_WithDifferentId_ShouldReturnForbidden() throws Exception{
        Long userId = 2L;

        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isForbidden());

        verify(userService, never()).deleteUser(any());
    }

    @Test
    @WithMockCustomUser(id = 1, roles = {"CUSTOMER"})
    void deleteById_AsCustomer_WithSameId_ShouldReturnNoContent() throws Exception{
        Long userId = 1L;

        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(userId);
    }

    @Test
    void deleteById_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).deleteUser(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteById_WithNonExistentId_ShouldReturnNotFound() throws Exception {
        Long userId = 999L;

        doThrow(new ResourceNotFoundException("User not found")).when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(userId);
    }

    /* ------ CREATE USER (ADMIN) ------ */
    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_AsAdmin_ShouldReturnCreated() throws Exception {
        String username = "customer";
        String email = "customer@example.com";

        UserCreateRequestDTO request = UserCreateRequestDTO.builder()
                .username(username)
                .email(email)
                .password("customer123")
                .firstName("customerFN")
                .lastName("customerLN")
                .role(USER_ROLE.CUSTOMER)
                .build();

        UserResponseDTO user = UserResponseDTO.builder()
                .id(1L)
                .username(username)
                .email(email)
                .role(USER_ROLE.CUSTOMER)
                .active(true)
                .build();

        when(userService.createUser(request)).thenReturn(user);

        mockMvc.perform(post("/api/v1/users")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email));

        verify(userService).createUser(request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_AsAdmin_IncorrectBody_ShouldReturnBadRequest() throws Exception {

        UserCreateRequestDTO request = UserCreateRequestDTO.builder()
                .username("cr") // corto (<3)
                .email("customerexample.com") // falta @
                .password("custome") // corto (<8)
                .firstName("customerFN")
                .lastName("customerLN")
                .role(null) // nulo
                .build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation field"))
                .andExpect(jsonPath("$.errors.username").exists())
                .andExpect(jsonPath("$.errors.email").exists())
                .andExpect(jsonPath("$.errors.password").exists())
                .andExpect(jsonPath("$.errors.role").exists());

        verify(userService, never()).createUser(request);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createUser_AsCustomer_ShouldReturnForbidden() throws Exception {
        UserCreateRequestDTO request = UserCreateRequestDTO.builder()
                .username("customer")
                .email("customer@example.com")
                .password("customer123")
                .firstName("customerFN")
                .lastName("customerLN")
                .role(USER_ROLE.CUSTOMER)
                .build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request))
                       )
                .andExpect(status().isForbidden());

        verify(userService, never()).createUser(any());
    }

    @Test
    void createUser_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/users"))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).createUser(any());
    }
}
