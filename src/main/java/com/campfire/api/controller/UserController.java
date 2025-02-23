package com.campfire.api.controller;

import com.campfire.api.entities.User;
import com.campfire.api.repository.UserRepository;
import com.campfire.api.dto.UserDTO;
import com.campfire.api.service.PasswordEncoderService;
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

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    public UserController(UserRepository userRepository, PasswordEncoderService passwordEncoderService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    @PostMapping(path = "/add", consumes = "application/json")
    public @ResponseBody ResponseEntity<String> addNewUser(@Valid @RequestBody UserDTO userRequest, BindingResult result) {

        if (result.hasErrors()) {
            return new ResponseEntity<>("Invalid data", HttpStatus.BAD_REQUEST);
        }

        try {
            User user = new User();
            String encodedPassword = passwordEncoderService.encode(userRequest.getPassword());

            user.setName(userRequest.getName());
            user.setEmail(userRequest.getEmail());
            user.setPassword(encodedPassword);
            System.out.println(encodedPassword);
            userRepository.save(user);

            return new ResponseEntity<>("User saved successfully", HttpStatus.CREATED);
        } catch (Exception error) {
            System.out.println(error);
            return new ResponseEntity<>("An error occurred while saving the user: " + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // Include error message
        }
    }
}