package app.training.security;

import app.training.dto.user.UserLoginRequestDto;
import app.training.dto.user.UserLoginResponseDto;
import app.training.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.email(), requestDto.password())
        );

        String token = jwtUtil.generateToken(authentication.getName());

        String roleName = authentication.getAuthorities().iterator().next().getAuthority();

        userService.unSubscribeUser(requestDto.email());

        return new UserLoginResponseDto(token, roleName);
    }
}
