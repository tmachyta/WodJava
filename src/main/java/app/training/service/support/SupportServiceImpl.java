package app.training.service.support;

import app.training.exception.EntityNotFoundException;
import app.training.model.User;
import app.training.repository.UserRepository;
import app.training.service.email.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {
    private static final Logger logger = LoggerFactory.getLogger(SupportServiceImpl.class);
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;

    @Override
    public void sendEmailToSupport(String email, String subject, String body) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find user by email " + email));

        emailSenderService.sendEmailToSupport(user.getEmail(), subject, body);

        logger.info("Mail successfully sent " + subject);
    }
}
