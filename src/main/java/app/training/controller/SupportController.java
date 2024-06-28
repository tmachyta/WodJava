package app.training.controller;

import app.training.dto.support.SupportEmailRequest;
import app.training.service.support.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management", description = "Endpoints for managing authentication")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/support")
public class SupportController {
    private final SupportService supportService;

    @PostMapping("/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @Operation(summary = "Send email to support",
            description = "Send email to support team")
    public void sendEmailToSupport(@PathVariable String email,
                                     @RequestBody SupportEmailRequest request) {
        supportService.sendEmailToSupport(email, request.getSubject(), request.getBody());
    }
}
