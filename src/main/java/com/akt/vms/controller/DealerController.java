package com.akt.vms.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dealer")
public class DealerController {
    @GetMapping
    public String helloDealer() {
        return "Hello Dealer!";
    }
}