package com.projects.taskManager.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("api/v1/testing-controller")
@RestController
public class TestController {

    @GetMapping
    @ResponseBody
    public String getMethodName(@RequestParam String jwtToken) {
        return "JwtToken received: " + jwtToken;
    }
    // @GetMapping
    // public String getMethodName() {
    //     return "Hello from the testing controller!";
    // }

    
}
