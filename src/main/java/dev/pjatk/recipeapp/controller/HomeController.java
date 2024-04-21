package dev.pjatk.recipeapp.controller;

import dev.pjatk.recipeapp.utils.Loggable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
