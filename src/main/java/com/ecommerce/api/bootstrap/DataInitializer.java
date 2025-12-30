package com.ecommerce.api.bootstrap;

import com.ecommerce.api.entity.UserEntity;
import com.ecommerce.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ecommerce.api.entity.UserEntity.USER_ROLE;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class DataInitializer  implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count()>0) return;

        UserEntity admin = UserEntity.builder()
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin123"))
                .firstName("Admin")
                .lastName("Admin")
                .role(USER_ROLE.ADMIN)
                .active(true)
                .build();
        UserEntity customer = UserEntity.builder()
                .username("customer")
                .email("customer@example.com")
                .password(passwordEncoder.encode("customer123"))
                .firstName("Customer")
                .lastName("Customer")
                .role(USER_ROLE.CUSTOMER)
                .active(true)
                .build();

        userRepository.save(admin);
        userRepository.save(customer);

        System.out.println("Usuarios creados:");
        System.out.println("Admin: admin@example.com / admin123");
        System.out.println("Customer: customer@example.com / customer123");
    }
}
