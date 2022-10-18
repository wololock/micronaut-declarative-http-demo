package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/demo/users")
final class DemoController {

    @GetMapping
    List<?> list() {
        return Collections.emptyList();
    }
}
