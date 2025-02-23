package com.campfire.api.config;

import com.campfire.api.repository.UserRepository;
import com.campfire.api.service.PasswordEncoderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository; // Inject UserRepository
    private final PasswordEncoderService passwordEncoderService;

    public SecurityConfig(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { // BCrypt is recommended
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/**").permitAll() // Libera a rota /user e seus subcaminhos
                        .anyRequest().authenticated() // Exige autenticação para todas as outras rotas
                )
                .csrf(csrf -> csrf.disable()); // Desabilita CSRF (opcional, dependendo do seu caso de uso)

        return http.build();
    }
}