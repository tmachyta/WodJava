package app.training.service.email;

public interface EmailSenderService {

    void sendEmail(String toEmail,
                   String subject,
                   String body);

    void sendEmailToSupport(String fromEmail,
                            String subject,
                            String body);
}
