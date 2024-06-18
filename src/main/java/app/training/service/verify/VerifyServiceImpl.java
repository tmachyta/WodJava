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
                "WODWarrior - verification",
                "Your verification is successful. Welcome to WODWarrior Family");
        return true;
    }
}
