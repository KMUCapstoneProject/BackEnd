package com.kdedevelop.actuatortest.controller.api;

import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldControllerApi {
    @GetMapping("hello-world")
    public String helloWorld() {
        return "HI!";
    }

    @GetMapping("hello")
    public String hello(@PathVariable String name) {
        return "Welcome! [|=USER=|]!".replace("[|=USER=|]", name);
    }
}
