package app.training.dto.support;

import lombok.Data;

@Data
public class SupportEmailRequest {
    private String subject;
    private String body;
}
