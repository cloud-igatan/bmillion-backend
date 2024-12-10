package com.example.bmillion_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping(value =  {"", "/", "/login", "/ThirdPage", "/Calender", "/ShowDiary"})
    public String forward() {
        return "forward:/index.html";
    }

}
