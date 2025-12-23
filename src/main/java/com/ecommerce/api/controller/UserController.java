package com.ecommerce.api.controller;

import com.ecommerce.api.dto.request.*;
import com.ecommerce.api.dto.response.UserResponseDTO;
import com.ecommerce.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
