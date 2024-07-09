package app.training.service.verify;

import app.training.exception.EntityNotFoundException;
import app.training.model.User;
import app.training.repository.UserRepository;
import app.training.service.email.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyServiceImpl implements VerifyService {
    private static final String STATUS = "Verified";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;

    @Override
    public boolean isVerified(String email) {
        User existedUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));
        existedUser.setStatus(STATUS);
        User savedUser = userRepository.save(existedUser);
        String body = """
                 Dear %s,
                 
                 Congratulations! Your account has been successfully verified.
                 
                 Welcome to the WODWarrior family! We're excited to have you on board 
                 
                 and can't wait to help you achieve your fitness goals.
                 
                 Here are some things you can do now:
                 
                 - Explore our exclusive training programs
                 
                 - Join live workout sessions
                 
                 - Access personalized fitness plans
                 
                 Thank you for choosing WODWarrior.
                 
                 Let's get started on your fitness journey!
                 
                 Best regards,
                 
                 The WODWarrior Team
                 
                """.formatted(existedUser.getFirstName()).replace("\n", LINE_SEPARATOR);
        emailSenderService.sendEmail(savedUser.getEmail(),
                "WODWarrior - Verification Successful",
                body);
        return true;
    }
}
