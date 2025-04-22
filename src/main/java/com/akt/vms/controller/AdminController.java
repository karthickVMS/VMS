package com.akt.vms.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String helloAdmin() {
        return "Hello Admin!";
    }
}