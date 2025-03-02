package com.campfire.api.controller;

import com.campfire.api.entities.User;
import com.campfire.api.repository.UserRepository;
import com.campfire.api.dto.UserDTO;
import com.campfire.api.service.PasswordEncoderService;
import com.campfire.api.service.JwtService;
import com.campfire.api.utils.ApiResponseBuilder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Import BCryptPasswordEncoder
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final JwtService jwtService;
    private final ApiResponseBuilder apiResponseBuilder;

    public UserController(UserRepository userRepository, PasswordEncoderService passwordEncoderService, BCryptPasswordEncoder bCryptPasswordEncoder, JwtService jwtService, ApiResponseBuilder apiResponseBuilder) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.jwtService = jwtService;
        this.apiResponseBuilder = apiResponseBuilder;
    }

    @PostMapping(path = "/add", consumes = "application/json")
    public @ResponseBody ResponseEntity<Object> addNewUser(@Valid @RequestBody UserDTO userRequest, BindingResult result) {

        if (result.hasErrors()) {
            return ApiResponseBuilder.buildErrorResponse("Invalid data");
        }

        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(userRequest.getEmail()));

        if (existingUser.isPresent()) {
            return ApiResponseBuilder.buildErrorResponse("User already exists");
        }

        try {
            User user = new User();
            String encodedPassword = passwordEncoderService.encode(userRequest.getPassword());

            user.setName(userRequest.getName());
            user.setEmail(userRequest.getEmail());
            user.setPassword(encodedPassword);

            userRepository.save(user);

            return ApiResponseBuilder.buildSuccessResponse("User saved successfully");
        } catch (Exception error) {
            return ApiResponseBuilder.buildErrorResponse("An error occurred while saving the user: " + error.getMessage());
        }
    }

    @PostMapping(path = "/auth", consumes = "application/json")
    public @ResponseBody ResponseEntity<?> authUser(@Valid @RequestBody UserDTO userRequest, BindingResult result) {

        if (result.hasErrors()) {
            return ApiResponseBuilder.buildErrorResponse("Invalid data");
        }

        try {
            User userSaved = userRepository.findByEmail(userRequest.getEmail());

            if (userSaved == null || !passwordEncoderService.matches(userRequest.getPassword(), userSaved.getPassword())) {
                return ApiResponseBuilder.buildErrorResponse("User or password not found");
            }

            String token = jwtService.generateToken(userSaved.getEmail());

            return ApiResponseBuilder.buildSuccessResponse("User authenticated with success", token);

        } catch (Exception error) {
            return ApiResponseBuilder.buildErrorResponse("An error occurred: " + error.getMessage());
        }
    }
}