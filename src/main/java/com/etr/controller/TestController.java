package com.etr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ypc
 * @Date: 2019/5/21 22:40
 * @Description:
 */
@RequestMapping("/test")
@RestController
public class TestController {
    @RequestMapping("/hello")
    public String test(){
        return "aa";
    }
}
