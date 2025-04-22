package com.akt.vms.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping
    public String helloUser() {
        return "Hello User!";
    }
}