package app.training.service.support;

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
class SupportServiceImplTest {
    private static final String EMAIL = "test@gmail.com";
    private static final String SUBJECT = "WODWarrior";
    private static final String BODY = "Test message";

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private SupportServiceImpl supportService;

    @Test
    public void testSendEmailToSupportSuccessfully() {
        User user = new User();
        user.setEmail(EMAIL);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        supportService.sendEmailToSupport(EMAIL, SUBJECT, BODY);
    }
}
