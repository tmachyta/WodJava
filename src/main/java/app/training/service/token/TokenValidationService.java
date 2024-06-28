package app.training.service.token;

public interface TokenValidationService {
    void blackListToken(String token);

    boolean isBlackListed(String token);
}
