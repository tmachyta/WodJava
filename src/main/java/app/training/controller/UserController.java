package app.training.controller;

import app.training.dto.user.UserRegistrationRequest;
import app.training.dto.user.UserResponseDto;
import app.training.dto.user.UserResponseRoleDto;
import app.training.dto.user.UserUpdateBirthdayRequest;
import app.training.dto.user.UserUpdateImageRequest;
import app.training.dto.user.UserUpdateLastNameRequest;
import app.training.dto.user.UserUpdateNameRequest;
import app.training.dto.user.UserUpdatePasswordRequest;
import app.training.exception.RegistrationException;
import app.training.model.Role;
import app.training.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all users", description = "Get a list of all available users")
    public List<UserResponseDto> findAll(Pageable pageable) {
        return userService.getAll(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/id/{id}")
    @Operation(summary = "Delete user by id", description = "Soft delete of available user by id")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @GetMapping("/me/id/{id}")
    @Operation(summary = "Get user by id", description = "Get available user by id")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @PutMapping("/me/id/{id}")
    @Operation(summary = "Update user by id", description = "update available user by id")
    public UserResponseDto updateUserInfo(@PathVariable Long id,
                                          @RequestBody @Valid UserRegistrationRequest request)
            throws RegistrationException {
        return userService.update(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/role/{id}")
    @Operation(summary = "Update user role by id",
            description = "Update available user role by id")
    public UserResponseRoleDto updateUserRoleById(@PathVariable Long id,
                                                  @RequestBody Role roleName) {
        return userService.updateRoleByUserId(id, roleName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/role/{email}")
    @Operation(summary = "Update user role by email",
            description = "Update available user role by email")
    public UserResponseRoleDto updateUserRoleEmail(@PathVariable String email,
                                                   @RequestBody Role roleName) {
        return userService.updateRoleByEmail(email, roleName);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @GetMapping("/me/{email}")
    @Operation(summary = "Get user by id", description = "Get available user by id")
    public UserResponseDto getUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @PutMapping("/me/{email}")
    @Operation(summary = "Update user image by email",
            description = "Update available user image by email")
    public UserResponseDto updateUserImage(@PathVariable String email,
                                           @RequestBody @Valid UserUpdateImageRequest request) {
        return userService.updateImage(email, request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @PutMapping("/me/name/{email}")
    @Operation(summary = "Update user name by email",
            description = "Update available user name by email")
    public UserResponseDto updateUserName(@PathVariable String email,
                                           @RequestBody @Valid UserUpdateNameRequest request) {
        return userService.updateName(email, request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @PutMapping("/me/last-name/{email}")
    @Operation(summary = "Update user last-name by email",
            description = "Update available user last-name by email")
    public UserResponseDto updateUserLastName(@PathVariable String email,
                                          @RequestBody @Valid UserUpdateLastNameRequest request) {
        return userService.updateLastName(email, request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @DeleteMapping("/me/{email}")
    @Operation(summary = "Delete user by email",
            description = "Soft delete of available user by email")
    public void deleteByEmail(@PathVariable String email) {
        userService.deleteByEmail(email);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @PutMapping("/me/password/{email}")
    @Operation(summary = "Update user password by email",
            description = "Update available user password by email")
    public UserResponseDto updateUserPassword(@PathVariable String email,
                                              @RequestBody @Valid
                                              UserUpdatePasswordRequest request) {
        return userService.updatePassword(email, request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUBSCRIBED')")
    @PutMapping("/me/birthday/{email}")
    @Operation(summary = "Update user birthday by email",
            description = "Update available user birthday by email")
    public UserResponseDto updateUserBirthday(@PathVariable String email,
                                              @RequestBody @Valid
                                              UserUpdateBirthdayRequest request) {
        return userService.updateBirthday(email, request);
    }
}
