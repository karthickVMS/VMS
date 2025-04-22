package com.akt.vms.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverController {
    @GetMapping
    public String helloDriver() {
        return "Hello Driver!";
    }
}