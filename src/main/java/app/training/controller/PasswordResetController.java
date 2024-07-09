package app.training.controller;

import app.training.service.password.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Password management", description = "Endpoints for reset password")
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/password")
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @PutMapping("/reset/{email}")
    @Operation(summary = "Restore user password by email",
            description = "Restore user password by email")
    public void resetPassword(@PathVariable String email) {
        passwordResetService.resetPassword(email);
    }
}
