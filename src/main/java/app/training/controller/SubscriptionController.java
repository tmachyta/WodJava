package app.training.controller;

import app.training.dto.stripe.RequestDto;
import app.training.service.token.TokenValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class SubscriptionController {
    private final TokenValidationService validationService;

    @Value("${stripe.api.publicKey}")
    private String publicKey;

    @GetMapping("/subscription")
    public String showCard(@ModelAttribute @Valid RequestDto requestDto,
                           Model model) {

        model.addAttribute("publicKey", publicKey);
        model.addAttribute("email", requestDto.getEmail());
        return "checkout";
    }

    @GetMapping("/payment")
    public String home() {
        return "successfulPayment";
    }
}


