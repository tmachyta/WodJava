package app.training.service.password;

import app.training.exception.EntityNotFoundException;
import app.training.model.User;
import app.training.repository.UserRepository;
import app.training.service.email.EmailSenderService;
import app.training.utils.TemporaryPasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;

    @Override
    public void resetPassword(String email) {
        User existedUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));

        String temporaryPassword = TemporaryPasswordUtil.generateTemporaryPassword();
        existedUser.setPassword(passwordEncoder.encode(temporaryPassword));
        User savedUser = userRepository.save(existedUser);

        emailSenderService.sendEmail(savedUser.getEmail(),
                "WODWarrior - reset password",
                "Yor temporary password " + temporaryPassword
                        + " Don't forget to change password after login");
    }
}
