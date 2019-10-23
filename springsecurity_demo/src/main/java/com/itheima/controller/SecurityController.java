package com.itheima.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class SecurityController {
    @RequestMapping("/test")
    public void test() {
        System.out.println("11111111");
    }
}
