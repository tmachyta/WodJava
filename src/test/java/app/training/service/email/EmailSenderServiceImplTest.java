package app.training.service.email;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceImplTest {
    private static final String EMAIL_FROM = "test@gmail.com";
    private static final String EMAIL_TO = "test2@gmail.com";
    private static final String SUBJECT = "WODWarrior";
    private static final String BODY = "Test message";

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailSenderServiceImpl emailSenderService;

    @Test
    public void testSendEmailSuccessfully() {

        emailSenderService.sendEmail(EMAIL_TO, SUBJECT, BODY);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL_FROM);
        message.setTo(EMAIL_TO);
        message.setSubject(SUBJECT);
        message.setText(BODY);

        Assertions.assertEquals(EMAIL_TO, message.getTo()[0]);
        Assertions.assertEquals(SUBJECT, message.getSubject());
        Assertions.assertEquals(BODY, message.getText());
    }

    @Test
    public void testSendEmailToSupportSuccessfully() {

        emailSenderService.sendEmailToSupport(EMAIL_FROM, SUBJECT, BODY);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL_FROM);
        message.setTo(EMAIL_TO);
        message.setSubject(SUBJECT);
        message.setText(BODY);

        Assertions.assertEquals(EMAIL_FROM, message.getFrom());
        Assertions.assertEquals(SUBJECT, message.getSubject());
        Assertions.assertEquals(BODY, message.getText());
    }
}
