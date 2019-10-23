package com.itheima.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('add')")
    public String add(){
        System.out.println("add......");
        return null;
    }

    @PreAuthorize("hasAuthority('delete')")
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public String delete(){
        System.out.println("addeleted......");
        return null;
    }

    @PreAuthorize("hasAuthority('update')")
    @RequestMapping(value = "/update",method = RequestMethod.GET)
    public String update(){
        System.out.println("update......");
        return null;
    }
}
