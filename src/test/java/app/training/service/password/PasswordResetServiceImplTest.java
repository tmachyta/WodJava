package app.training.service.password;

import static org.mockito.Mockito.when;

import app.training.model.User;
import app.training.repository.UserRepository;
import app.training.service.email.EmailSenderService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceImplTest {
    private static final String EMAIL = "test@gmail.com";
    private static final String TEMPORARY_PASSWORD = "Tfjf78912kkh";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private PasswordResetServiceImpl passwordResetService;

    @Test
    public void testResetPasswordSuccessfully() {
        User user = new User();
        user.setEmail(EMAIL);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        user.setPassword(passwordEncoder.encode(TEMPORARY_PASSWORD));

        when(userRepository.save(user)).thenReturn(user);

        passwordResetService.resetPassword(EMAIL);
    }
}
