package com.campfire.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloApiController {
    @GetMapping("/hello")

    public String get() {
        return "hello teste";
    }
}
