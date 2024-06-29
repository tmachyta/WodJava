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
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;

    @Override
    public boolean isVerified(String email) {
        User existedUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));
        existedUser.setStatus(STATUS);
        User savedUser = userRepository.save(existedUser);
        emailSenderService.sendEmail(savedUser.getEmail(),
                "WODWarrior - Verification Successful",
                "Dear " + savedUser.getFirstName() + ",\n\n"
                        + "Congratulations! Your account has been successfully verified.\n\n"
                        + "Welcome to the WODWarrior family! We're excited to have you on board "
                        + "and can't wait to help you achieve your fitness goals.\n\n"
                        + "Here are some things you can do now:\n"
                        + "- Explore our exclusive training programs\n"
                        + "- Join live workout sessions\n"
                        + "- Access personalized fitness plans\n\n"
                        + "Thank you for choosing WODWarrior. "
                        + "Let's get started on your fitness journey!\n\n"
                        + "Best regards,\n"
                        + "The WODWarrior Team");
        return true;
    }
}
