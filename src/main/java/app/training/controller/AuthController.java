package app.training.controller;

import app.training.dto.user.UserLoginRequestDto;
import app.training.dto.user.UserLoginResponseDto;
import app.training.dto.user.UserRegistrationRequest;
import app.training.dto.user.UserResponseDto;
import app.training.exception.RegistrationException;
import app.training.security.AuthenticationService;
import app.training.service.password.PasswordResetService;
import app.training.service.token.TokenValidationService;
import app.training.service.user.UserService;
import app.training.service.verify.VerifyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication management", description = "Endpoints for managing authentication")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final PasswordResetService passwordResetService;
    private final VerifyService verifyService;
    private final TokenValidationService validationService;

    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "Login", description = "Login method to authenticate users")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/register")
    @PermitAll
    @Operation(summary = "Register", description = "Register method to register users")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequest request)
            throws RegistrationException {
        return userService.register(request);
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "Logout", description = "Logout method")
    public void logoutUser(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        validationService.blackListToken(token);
    }

    @PutMapping("/reset/{email}")
    @Operation(summary = "Restore user password by email",
            description = "Restore user password by email, with no auth")
    public void resetPassword(@PathVariable String email) {
        passwordResetService.resetPassword(email);
    }

    @GetMapping("/check/{email}")
    @Operation(summary = "Check email", description = "Check if email is ready to use")
    public void checkEmail(@PathVariable String email) {
        userService.findUserByEmail(email);
    }

    @PutMapping("/verify/{email}")
    @Operation(summary = "Verify user email",
            description = "Verify user email by code")
    public void verifyAcc(@PathVariable String email,
                            @RequestParam("code") String code) {
        verifyService.isVerified(email);
    }
}
