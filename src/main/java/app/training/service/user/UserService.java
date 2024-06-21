package app.training.service.user;

import app.training.dto.user.UserRegistrationRequest;
import app.training.dto.user.UserResponseDto;
import app.training.dto.user.UserResponseRoleDto;
import app.training.dto.user.UserUpdateImageRequest;
import app.training.dto.user.UserUpdateLastNameRequest;
import app.training.dto.user.UserUpdateNameRequest;
import app.training.dto.user.UserUpdatePasswordRequest;
import app.training.exception.RegistrationException;
import app.training.model.Role;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto register(UserRegistrationRequest request) throws RegistrationException;

    List<UserResponseDto> getAll(Pageable pageable);

    void deleteById(Long id);

    UserResponseDto findById(Long id);

    UserResponseRoleDto updateRoleByEmail(String email, Role roleName);

    UserResponseRoleDto updateRoleByUserId(Long id, Role roleName);

    UserResponseDto update(Long id, UserRegistrationRequest requestDto)
            throws RegistrationException;

    UserResponseDto findUserByEmail(String email);

    UserResponseDto updateImage(String email, UserUpdateImageRequest request);

    UserResponseDto updateName(String email, UserUpdateNameRequest request);

    UserResponseDto updateLastName(String email, UserUpdateLastNameRequest request);

    void deleteByEmail(String email);

    UserResponseDto updatePassword(String email, UserUpdatePasswordRequest request);
}
