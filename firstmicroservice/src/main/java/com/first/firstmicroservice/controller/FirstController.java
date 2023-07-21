package com.first.firstmicroservice.controller;

import com.first.firstmicroservice.model.AuthenticatedUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first")
public class FirstController {

    @GetMapping("/message")
    public String message(){
        return "Hello from First Microservice";
    }

    @GetMapping("/authenticate/{userId}")
    public AuthenticatedUser authenticateUser(@PathVariable String userId){
        System.out.println("UserId "+ userId);
        //return "aletisil";
        return new AuthenticatedUser(userId, "989ui765rt");
    }
}
