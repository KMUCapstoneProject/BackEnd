package com.kdedevelop.actuatortest.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldControllerApi {
    @GetMapping("hello-world")
    public String helloWorld() {
        return "HI!";
    }
}
