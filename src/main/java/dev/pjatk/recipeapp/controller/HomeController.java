package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.util.Loggable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class HomeController implements Loggable {

    @GetMapping
    public String home() {
        log().info("Welcome to Recipe App!");
        return "Welcome to Recipe App!";
    }

}
