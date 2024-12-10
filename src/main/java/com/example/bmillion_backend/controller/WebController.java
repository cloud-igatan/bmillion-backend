package com.example.bmillion_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping({"", "/", "/login", "/ThirdPage", "/Calender", "/ShowDiary"})
    public String index() {
        return "index.html";
    }

}
