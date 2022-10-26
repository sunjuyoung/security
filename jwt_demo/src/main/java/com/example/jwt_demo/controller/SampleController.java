package com.example.jwt_demo.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sample")
public class SampleController {

    @ApiOperation("Sample Get")
    @GetMapping("/test")
    public String test(){
        return "halo";
    }
}
