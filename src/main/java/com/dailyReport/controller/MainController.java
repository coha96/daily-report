package com.dailyReport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // http://localhost:8080/ 방문 시 해당 메서드 호출
    @GetMapping(value = {"","/"})
    public String main()
    {
        return "main";
    }
}