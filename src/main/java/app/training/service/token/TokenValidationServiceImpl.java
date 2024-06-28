package app.training.service.token;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenValidationServiceImpl implements TokenValidationService {
    private Set<String> blackListTokenSet = new HashSet<>();

    @Override
    public void blackListToken(String token) {
        blackListTokenSet.add(token);
    }

    @Override
    public boolean isBlackListed(String token) {
        return blackListTokenSet.contains(token);
    }
}
