package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/bonus/users")
final class BonusController {

    @GetMapping
    List<?> list() {
        return Collections.emptyList();
    }
}
