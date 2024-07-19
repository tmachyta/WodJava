package app.training.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class PartnersController {
    @GetMapping("/partners")
    public String home() {
        return "partners";
    }
}
