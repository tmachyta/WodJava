package app.training.service.verify;

import static org.junit.jupiter.api.Assertions.assertTrue;
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

@ExtendWith(MockitoExtension.class)
class VerifyServiceImplTest {
    private static final String EMAIL = "test@gmail.com";
    private static final String STATUS = "Verified";

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private VerifyServiceImpl verifyService;

    @Test
    public void testVerifyUserSuccessfully() {
        User user = new User();
        user.setEmail(EMAIL);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        user.setStatus(STATUS);

        when(userRepository.save(user)).thenReturn(user);

        boolean isVerified = verifyService.isVerified(EMAIL);

        assertTrue(isVerified);

        /*verify(emailSenderService).sendEmail(
                eq(user.getEmail()),
                eq("WODWarrior - verification"),
                eq("Your verification is successful. Welcome to WODWarrior Family")
        );*/
    }
}
