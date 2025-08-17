package com.agn1kobi.e_commerce_backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {

    @GetMapping("hello-world")
    public String helloWorld() {
        return "Hello World!";
    }
}
